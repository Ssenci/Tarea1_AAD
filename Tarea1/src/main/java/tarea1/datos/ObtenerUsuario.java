package tarea1.datos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ObtenerUsuario {
	public static ArrayList<String[]> obtenerUsuarios() throws IOException {
		ArrayList<String[]> ListaUsuario = new ArrayList<String[]>();

		BufferedReader br = new BufferedReader(new FileReader("archivos/credenciales.txt"));

		String line;

		while ((line = br.readLine()) != null) {

			String[] aux = line.split(",");

			ListaUsuario.add(aux);

		}
		br.close();

		return ListaUsuario;
	}
}
