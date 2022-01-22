package jpp.numbergame;

public class Tile {
	private Coordinate2D coord;
	private int value;
	public Tile(Coordinate2D coord, int value) {
		this.coord = coord;
		this.value = value;
	}
	public Coordinate2D getCoord() {
		return coord;
	}
	public int getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coord == null) ? 0 : coord.hashCode());
		result = prime * result + value;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (coord == null) {
			if (other.coord != null)
				return false;
		} else if (!coord.equals(other.coord))
			return false;
		if (value != other.value)
			return false;
		return true;
	}
	
	
}
