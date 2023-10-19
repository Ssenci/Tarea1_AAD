package tarea1.logica;

import java.time.LocalDate;

public class Estancia {

	private long id;
	private LocalDate fecha;
	private boolean VIP = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public boolean isVIP() {
		return VIP;
	}

	public void setVIP(boolean vIP) {
		VIP = vIP;
	}

	public Estancia(long id, LocalDate fecha, boolean vIP) {
		super();
		this.id = id;
		this.fecha = fecha;
		VIP = vIP;
	}

}
