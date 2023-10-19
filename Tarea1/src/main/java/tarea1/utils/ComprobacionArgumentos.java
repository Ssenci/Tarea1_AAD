package tarea1.utils;

public class ComprobacionArgumentos {

	public static void esNulo(Object arg) {
		if (arg == null)
			throw new IllegalArgumentException("Error:campo nulo");
	}

	public static void esVacio(String arg) {
		if (arg.trim().length() == 0)
			throw new IllegalArgumentException("Error:campo vacio");
	}

	public static void esInvalido(String arg) {
		if (arg.toUpperCase().equals("NULL"))
			throw new IllegalArgumentException("Error:campo invalido");
	}
	
	public static void comprueba(boolean arg, String msg) {
		if (arg)
			throw new IllegalArgumentException(msg);
	}
}
