# Custom Great Ghoul Duel Server

A custom server for the game "Great Ghoul Duel".

You can run it with the following command:

```
./gradlew run
```

This server will run on port 8080, regardless of what port you want it to run on.
As of the time of writing this README, the match starts when two clients have connected.

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