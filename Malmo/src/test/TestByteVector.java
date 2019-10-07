package test;

import com.microsoft.msr.malmo.ByteVector;

public class TestByteVector {
	public static void main(String args[]) {
		ByteVector b = new ByteVector();
		b.add((short) 1);
		
		System.out.println(b.get(0));
	}
}
