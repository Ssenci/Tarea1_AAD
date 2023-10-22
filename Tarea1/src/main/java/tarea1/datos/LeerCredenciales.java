package tarea1.datos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import tarea1.logica.Peregrino;



public class LeerCredenciales {
	
	public static ArrayList<Peregrino> obtenerPeregrino() throws IOException {

		ArrayList<Peregrino> credenciales= new ArrayList<Peregrino>();
		// 1º paso: localizar el fichero a leer
		BufferedReader br = new BufferedReader(new FileReader("archivos/credenciales.txt"));

		// 2º paso: una vez localizado leerlo(linea a linea)
		String line;

		while ((line = br.readLine()) != null) {
			// 3º paso: obtener los elementos de la parada de cada linea
			String[] aux = line.split(",");
			long id = Long.parseLong(aux[3]);
			String nombre = aux[0];
			

			Peregrino peregrino = new Peregrino(id,nombre);
			// 4º paso: guardar las paradas al arrayList y devolverlo
			credenciales.add(peregrino);

		}
		br.close();
		return credenciales;
	}
	
	
}
