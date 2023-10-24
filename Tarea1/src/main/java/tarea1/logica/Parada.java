package tarea1.logica;

public class Parada {

	private long id;
	private String nombre;
	private String region;
	private String nombreUsuario;

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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public Parada(long id) {
		super();
		this.id = id;
	}
	
	public Parada(String nombre) {
		super();
		this.nombre = nombre;
	}

	public Parada(long id, String nombre, String region) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.region = region;
	}

	public Parada(long id, String nombre, String region, String nombreUsuario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.region = region;
		this.nombreUsuario = nombreUsuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Parada other = (Parada) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
