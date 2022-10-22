# Custom Great Ghoul Duel Server

A custom server for the game "Great Ghoul Duel".

You can run it with the following command:

```
./gradlew run
```

Alternatively, you can build the project:

```
./gradlew build
```

And then run it as so:

```
java -DMAX_PLAYERS=${MAX_PLAYERS} -jar build/libs/GhostGame-all.jar --csstore </path/to/keystore.p12> -csp <keystorepassword> -p 8080
```

By default, the server will run on 8080 in non-TLS mode and the match will start when two clients have connected.
This can be changed using command line flags. Use `--help` for more information.

You can access Great Ghoul Duel [here](https://www.google.com/logos/2021/halloween18_reboot/r1/halloween18_reboot.html).

To connect to the custom server, create a TamperMonkey script with the following code (and reload) or run the following in console:

```javascript
// ==UserScript==
// @name GGD mock
// @version 0.1
// @description Fan Server
// @author Jason Gronn
// @match https://www.google.com/logos/2021/halloween18_reboot/r1/halloween18_reboot.html
// @grant none
// ==/UserScript==

sessionStorage.clear();
let NativeWebSocket = WebSocket;
window.WebSocket = class WebSocket {
	#internalWS;
	constructor(url, d) {
		console.log("Mocking", url);

		this.#internalWS = new NativeWebSocket("ws://localhost:8080/");
		this.#internalWS.binaryType = "arraybuffer";
		this.#internalWS.onopen = () => {
			this.#internalWS.send(url.replace("ws://", "").replace("wss://", ""));
			this.onopen();
		}
		this.#internalWS.onmessage = m => this.onmessage(m);
	}
	send(packet) {
		this.#internalWS.send(packet);
	}
	close() {
		this.#internalWS.close();
	}
};
```