package tarea1.datos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import tarea1.logica.Parada;

public class LeerDAT {

	public static ArrayList<Parada> obtenerParadas() throws IOException {

		ArrayList<Parada> paradas = new ArrayList<Parada>();
		// 1º paso: localizar el fichero a leer
		BufferedReader br = new BufferedReader(new FileReader("\\archivos\\paradas.dat"));

		// 2º paso: una vez localizado leerlo(linea a linea)
		String line;

		while ((line = br.readLine()) != null) {
			// 3º paso: obtener los elementos de la parada de cada linea
			String[] aux = line.split(",");
			Long id = Long.parseLong(aux[0]);
			String nombre = aux[1];
			char region = aux[2].charAt(0);

			Parada parada = new Parada(id, nombre, region);
			// 4º paso: guardar las paradas al arrayList y devolverlo
			paradas.add(parada);

		}
		br.close();
		return paradas;
	}

}
