package tarea1.datos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CrearDAT {

	public static void añadirParada(long id, String nombre, String region, String nombreUsuario) throws IOException {
		//localizar el archivo
		BufferedWriter bw = new BufferedWriter(new FileWriter("archivos/paradas.dat",true));
		//guardar datos en él
		bw.write("\n" + id + "," + nombre + "," + region + "," + nombreUsuario);
		bw.close();
	}
}
