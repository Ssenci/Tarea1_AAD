package tarea1.datos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CrearCredenciales {
	public static void añadirCredenciales(String usuario, String contraseña, String peregrino, long id, String nacionalidad, long idparada) throws IOException {
		//localizar el archivo
		BufferedWriter bw = new BufferedWriter(new FileWriter("archivos/credenciales.txt",true));
		//guardar datos en él
		bw.write("\n" + usuario + "," + contraseña + "," + peregrino + "," + id + "," + nacionalidad + "," + idparada);
		bw.close();
	}
	
}
