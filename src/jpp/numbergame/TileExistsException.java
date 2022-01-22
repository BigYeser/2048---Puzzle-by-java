package jpp.numbergame;

public class TileExistsException extends RuntimeException{
	private String message;
	public TileExistsException() {
		super();
	}

	public TileExistsException(String message) {
		super(message);
		this.message = message;
	}
	

}
