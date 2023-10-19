package tarea1.logica;

import java.time.LocalDate;

public class Carnet {

	private long idperegrino;
	private LocalDate fechaexp;
	private double distancio = 0.0;
	private int nvips = 0;

	public long getIdperegrino() {
		return idperegrino;
	}

	public void setIdperegrino(long idperegrino) {
		this.idperegrino = idperegrino;
	}

	public LocalDate getFechaexp() {
		return fechaexp;
	}

	public void setFechaexp(LocalDate fechaexp) {
		this.fechaexp = fechaexp;
	}

	public double getDistancio() {
		return distancio;
	}

	public void setDistancio(double distancio) {
		this.distancio = distancio;
	}

	public int getNvips() {
		return nvips;
	}

	public void setNvips(int nvips) {
		this.nvips = nvips;
	}

	public Carnet(long idperegrino, LocalDate fechaexp, double distancio, int nvips) {
		super();
		this.idperegrino = idperegrino;
		this.fechaexp = fechaexp;
		this.distancio = distancio;
		this.nvips = nvips;
	}

}
