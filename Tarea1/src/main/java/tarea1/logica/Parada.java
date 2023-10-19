package tarea1.logica;

public class Parada {

	private long id;
	private String nombre;
	private char region;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public char getRegion() {
		return region;
	}

	public void setRegion(char region) {
		this.region = region;
	}

	public Parada(long id, String nombre, char region) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.region = region;
	}

}
