package everyos.ggd.server.socket.decoder.imp;

import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.SocketArray.RawEntry;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.imp.SocketArrayImp;

public class SocketDecoderImp implements SocketDecoder {

	@Override
	public int decodeNumber(byte[] bs, int offset) {
		int num = 0;
		for (int i = 0; ; i++) {
			int part = bs[i + offset] & ((1 << 7) - 1);
			num += part << (7 * i);
			if ((bs[i + offset] & (1 << 7)) == 0) {
				break;
			}
		}
		
		return num;
	}

	@Override
	public String decodeString(byte[] bs, int offset, int length) {
		return new String(bs, offset, length);
	}

	@Override
	public boolean decodeBoolean(byte[] bs, int offset) {
		return bs[offset] > 0;
	}

	@Override
	public SocketArray decodeArray(byte[] bs, int offset, int length) {
		SocketArray array = new SocketArrayImp(this, null);
		for (int target = offset + length; offset < target;) {
			int numLength = getNumberLength(bs, offset);
			int indexInfo = decodeNumber(bs, offset);
			int index = (indexInfo >>> 3) - 1;
			offset += numLength;
			
			RawEntry entry = getEntryAtIndex(bs, offset, indexInfo);
			array.setRaw(index, entry);
			offset = entry.offset() + entry.length();
		}
		
		return array;
	}

	private RawEntry getEntryAtIndex(byte[] bs, int offset, int indexInfo) {
		if ((indexInfo & 3) == 0) {
			int length = getNumberLength(bs, offset);
			return new RawEntry(bs, offset, length, 0);
		} else {
			int length = decodeNumber(bs, offset);
			offset += getNumberLength(bs, offset);
			return new RawEntry(bs, offset, length, 2);
		}
	}

	private int getNumberLength(byte[] bs, int offset) {
		int len = 0;
		while ((bs[len + offset] & (1 << 7)) > 0) {
			len++;
		}
		len++;
		
		return len;
	}

}
