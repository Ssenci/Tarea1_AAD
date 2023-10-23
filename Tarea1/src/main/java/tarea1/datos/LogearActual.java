package tarea1.datos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import tarea1.logica.Peregrino;

public class LogearActual {
	public static Peregrino logearActual(String usuario, String contraseña) {
		ArrayList<String[]> listaCredenciales = new ArrayList<String[]>();
		
		try {
			listaCredenciales = ObtenerUsuario.obtenerUsuarios();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		for (int i = 0; i < listaCredenciales.size(); i++) {
			String[] lista = listaCredenciales.get(i);
			if(lista[0].equals(usuario) && lista[1].equals(contraseña)) {
				return new Peregrino(lista[0], lista[2]);
			}
		}
		return null;
		
	}
}
