package tarea1.logica;

public class PeregrinoParada {

	private long idPeregrionoParada;
	private String nombreperegrinoParada;
	private String nacionalidad;
	private char region;

	public long getIdPeregrionoParada() {
		return idPeregrionoParada;
	}

	public void setIdPeregrionoParada(long idPeregrionoParada) {
		this.idPeregrionoParada = idPeregrionoParada;
	}

	public String getNombreperegrinoParada() {
		return nombreperegrinoParada;
	}

	public void setNombreperegrinoParada(String nombreperegrinoParada) {
		this.nombreperegrinoParada = nombreperegrinoParada;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public char getRegion() {
		return region;
	}

	public void setRegion(char region) {
		this.region = region;
	}

	public PeregrinoParada(long idPeregrionoParada, String nombreperegrinoParada, String nacionalidad, char region) {
		super();
		this.idPeregrionoParada = idPeregrionoParada;
		this.nombreperegrinoParada = nombreperegrinoParada;
		this.nacionalidad = nacionalidad;
		this.region = region;
	}

}
