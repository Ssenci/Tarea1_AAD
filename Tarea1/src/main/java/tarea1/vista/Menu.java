package tarea1.vista;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import tarea1.datos.CrearCredenciales;
import tarea1.datos.CrearDAT;
import tarea1.datos.LeerCredenciales;
import tarea1.datos.LeerDAT;
import tarea1.datos.LeerXml;
import tarea1.datos.LogearActual;
import tarea1.datos.ObtenerUsuario;
import tarea1.logica.Parada;
import tarea1.logica.Peregrino;
import tarea1.utils.ComprobacionArgumentos;

public class Menu {

	private Scanner teclado;
	private Peregrino usuarioLogeado;

	public Menu() {
		teclado = new Scanner(System.in);

	}

	public void run() {
		System.out.println("1-Registrarse como peregrino 2-Login 3-Salir");
		int res = teclado.nextInt();
		teclado.nextLine(); // limpieza de buffer

		while (res != 3) {
			if (res == 1) {
				this.registrar();

			}
			if (res == 2) {
				this.login();
			}

			System.out.println("1-Registrarse como peregrino 2-Login 3-Salir");
			res = teclado.nextInt();
			teclado.nextLine();
		}
	}

	public void registrar() {

		boolean aux = false;
		String usuario = " ";
		String contraseña = " ";
		String nacionalidad = " ";
		int idParadaActual = 0;
		String respuesta = " ";
		ArrayList<Parada> listaParadas = new ArrayList<Parada>();
		ArrayList<Peregrino> listaPeregrinos = new ArrayList<Peregrino>();
		ArrayList<Peregrino> listaCredenciales = new ArrayList<Peregrino>();
		long idMayor = Long.MIN_VALUE;

		try {
			listaParadas = LeerDAT.obtenerParadas();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		do {
			try {
				System.out.print("Nombre de usuario: ");

				usuario = teclado.nextLine();

				ComprobacionArgumentos.esNulo(usuario);
				ComprobacionArgumentos.esVacio(usuario);
				ComprobacionArgumentos.esInvalido(usuario);

				System.out.print("Contraseña: ");

				contraseña = teclado.nextLine();

				ComprobacionArgumentos.esNulo(contraseña);
				ComprobacionArgumentos.esVacio(contraseña);
				ComprobacionArgumentos.esInvalido(contraseña);
				ComprobacionArgumentos.comprueba(contraseña.length() > 16, "contraseña demasiado larga");

				System.out.print("Nacionalidad: ");

				nacionalidad = teclado.nextLine();

				ComprobacionArgumentos.esNulo(nacionalidad);
				ComprobacionArgumentos.esVacio(nacionalidad);
				ComprobacionArgumentos.esInvalido(nacionalidad);
				ComprobacionArgumentos.comprueba(!LeerXml.obtenerPaises().contains(nacionalidad),
						"La nacionalidad no concuerda con ninguna nacionalidad de la lista");
				// asociar parada a peregrino(pregunta al usuario la parada (id) y asociarsela)
				System.out.print("Dime la id de la parada desde la que te registras: ");

				idParadaActual = teclado.nextInt();

				// Comprabar que no sea null(validacion) "en este caso no hace falta validar"
				// Comprobar que idparadaactual este dentro de las id´s de paradas.dat
				// (verificacion)

				ComprobacionArgumentos.comprueba(!listaParadas.contains(new Parada(idParadaActual)),
						"Error: la id que has pasado, no existe");

				System.out.print("Los datos introducidos son correctos? (S/N)");
				teclado.nextLine();
				respuesta = teclado.nextLine();

				ComprobacionArgumentos.esNulo(respuesta);
				ComprobacionArgumentos.esVacio(respuesta);
				ComprobacionArgumentos.esInvalido(respuesta);
				ComprobacionArgumentos.comprueba(respuesta.length() > 1,
						"Error: respuesta damasiado larga, usa S para si y N para no");
				ComprobacionArgumentos.comprueba(!respuesta.equals("S") && !respuesta.equals("s"),
						"Vuelve a introducir todos los datos de nuevo");

				aux = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				aux = true;
			}
		} while (aux == true);

		// Comparar los datos asociados al nuevo peregrino con el fichero
		// credenciales.txt
		// en caso de existir syso:Error. usuario ya existente. en caso de no existir
		// añadir al txt y logearlo automaticamente

		System.out.println("Comparando campos con ficheros...");
		try {
			listaCredenciales = LeerCredenciales.obtenerPeregrino();
			ComprobacionArgumentos.comprueba(listaCredenciales.contains(new Peregrino(usuario)),
					"Error: Usuario en uso, repite el proceso utilizando otro nombre distinto");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;

		}

		System.out.println("Campos correctos y libres, iniciando sesion...");

		// asociar campos a peregrino
		for (Peregrino a : listaCredenciales) {
			if (a.getId() > idMayor)
				idMayor = a.getId();

		}
		try {
			CrearCredenciales.añadirCredenciales(usuario, contraseña, "peregrino", idMayor + 1, nacionalidad,
					idParadaActual);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		this.login(usuario, contraseña);
	}

	public void login() {
		ArrayList<String[]> credenciales = new ArrayList<String[]>();
		String[] datos = new String[6];
		boolean aux = false;
		String nombre = "";
		String contraseña = "";
		try {
			credenciales = ObtenerUsuario.obtenerUsuarios();
		} catch (IOException e) {
			e.printStackTrace();
		}

		do {
			System.out.print("Introduce nombre: ");
			nombre = teclado.nextLine();
			System.out.print("Introduce contraseña: ");
			contraseña = teclado.nextLine();

			try {
				for (int i = 0; i < credenciales.size(); i++) {
					if (credenciales.get(i)[0].equals(nombre)) {
						ComprobacionArgumentos.comprueba(
								!(credenciales.get(i)[0].equals(nombre) && credenciales.get(i)[1].equals(contraseña)),
								"Error: Usuario o contraseña invalido");
					}
				}
				aux = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				aux = true;

			}
		} while (aux == true);
		this.login(nombre, contraseña);
	}

	public void login(String usuario, String contraseña) {

		usuarioLogeado = LogearActual.logearActual(usuario, contraseña);
		// menus de perfiles
		if (usuarioLogeado != null && usuarioLogeado.getPerfil().equals("peregrino")) {
			this.menuPeregrino();
		} else if (usuarioLogeado != null && usuarioLogeado.getPerfil().equals("admingeneral")) {
			this.menuAdminGeneral();
		} else if (usuarioLogeado != null && usuarioLogeado.getPerfil().equals("parada")) {
			this.menuParada();
		} else {
			System.out.println("Error no existe usuario");
			this.login();
		}
	}

	public void menuPeregrino() {
		ArrayList<String[]> listaPeregrino = new ArrayList<String[]>();
		ArrayList<Parada> listaParadas = new ArrayList<Parada>();
		String[] datos = new String[6];
		String[] datosP = new String[3];
		int res;
		try {
			listaPeregrino = ObtenerUsuario.obtenerUsuarios();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			listaParadas = LeerDAT.obtenerParadas();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < listaPeregrino.size(); i++) {
			if (listaPeregrino.get(i)[0].equals(usuarioLogeado.getNombre())) {
				datos[0] = listaPeregrino.get(i)[0];
				datos[1] = listaPeregrino.get(i)[1];
				datos[2] = listaPeregrino.get(i)[2];
				datos[3] = listaPeregrino.get(i)[3];
				datos[4] = listaPeregrino.get(i)[4];
				datos[5] = listaPeregrino.get(i)[5];
			}
		}

		for (int i = 0; i < listaParadas.size(); i++) {
			if (Long.parseLong(datos[5]) == listaParadas.get(i).getId()) {

				datosP[1] = listaParadas.get(i).getNombre();
				datosP[2] = listaParadas.get(i).getRegion();

			}
		}
		// Obtener el valor de los atributos del peregrino que se acaba de registrar
		System.out.println("Bienvenido al sistema, sus datos son los siguientes: ID: " + datos[3] + ", nombre: "
				+ datos[0] + "" + ", nacionalidad: " + datos[4] + ", fecha de expedición : " + LocalDateTime.now()
				+ ", nombre + región de la parada inicial: " + datosP[1] + " " + datosP[2]);

		System.out.println("1-Exportar carnet en XML 2-Logout");
		res = teclado.nextInt();
		while (res != 3) {
			if (res == 1) {
				// exportar carnet en XML
			}
			if (res == 2) {
				// logout
				// logut establezco el usuario que esta conectado a null
				usuarioLogeado = null;
				this.run();

			}

		}

	}

	public void menuAdminGeneral() {
		ArrayList<String[]> listaPeregrino = new ArrayList<String[]>();
		ArrayList<Parada> listaParadas = new ArrayList<Parada>();
		String[] datos = new String[6];
		String[] datosP = new String[3];
		int res;
		try {
			listaPeregrino = ObtenerUsuario.obtenerUsuarios();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			listaParadas = LeerDAT.obtenerParadas();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < listaPeregrino.size(); i++) {
			if (listaPeregrino.get(i)[0].equals(usuarioLogeado.getNombre())) {
				datos[0] = listaPeregrino.get(i)[0];
				datos[1] = listaPeregrino.get(i)[1];
				datos[2] = listaPeregrino.get(i)[2];
				datos[3] = listaPeregrino.get(i)[3];
				datos[4] = listaPeregrino.get(i)[4];
				datos[5] = listaPeregrino.get(i)[5];
			}
		}

		for (int i = 0; i < listaParadas.size(); i++) {
			if (Long.parseLong(datos[5]) == listaParadas.get(i).getId()) {

				datosP[1] = listaParadas.get(i).getNombre();
				datosP[2] = listaParadas.get(i).getRegion();

			}
		}
		// Obtener el valor de los atributos del peregrino que se acaba de registrar
		System.out.println("Bienvenido al sistema, sus datos son los siguientes: ID: " + datos[3] + ", nombre: "
				+ datos[0] + "" + ", nacionalidad: " + datos[4] + ", fecha de expedición : " + LocalDateTime.now()
				+ ", nombre + región de la parada inicial: " + datosP[1] + " " + datosP[2]);

		System.out.println("1-Registrar parada 2-Logout");
		res = teclado.nextInt();
		String nombreP;
		String codigoP;
		long idMayor = Long.MIN_VALUE;
		while (res != 3) {
			if (res == 1) {
				// registrar parada
				// su identificador propio, su nombre y código de región, y el nombre del
				// usuario que es administrador/responsable de la misma
				System.out.println("Dime nombre de la parada: ");
				nombreP = teclado.nextLine();

				ComprobacionArgumentos.esNulo(nombreP);
				ComprobacionArgumentos.esVacio(nombreP);
				ComprobacionArgumentos.esInvalido(nombreP);

				System.out.println("Codigo de la region: ");
				codigoP = teclado.nextLine();

				ComprobacionArgumentos.esNulo(codigoP);
				ComprobacionArgumentos.esVacio(codigoP);
				ComprobacionArgumentos.esInvalido(codigoP);

				for (Parada a : listaParadas) {
					if (a.getId() > idMayor)
						idMayor = a.getId();

				}

				try {
					CrearDAT.añadirParada(idMayor + 1, nombreP, codigoP, usuarioLogeado.getNombre());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (res == 2) {
				// logout
				// logut establezco el usuario que esta conectado a null
				usuarioLogeado = null;
				this.run();
			}

		}

	}

	public void menuParada() {
		ArrayList<String[]> listaPeregrino = new ArrayList<String[]>();
		ArrayList<Parada> listaParadas = new ArrayList<Parada>();
		String[] datos = new String[6];
		String[] datosP = new String[3];
		int res;
		try {
			listaPeregrino = ObtenerUsuario.obtenerUsuarios();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			listaParadas = LeerDAT.obtenerParadas();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < listaPeregrino.size(); i++) {
			if (listaPeregrino.get(i)[0].equals(usuarioLogeado.getNombre())) {
				datos[0] = listaPeregrino.get(i)[0];
				datos[1] = listaPeregrino.get(i)[1];
				datos[2] = listaPeregrino.get(i)[2];
				datos[3] = listaPeregrino.get(i)[3];
				datos[4] = listaPeregrino.get(i)[4];
				datos[5] = listaPeregrino.get(i)[5];
			}
		}

		for (int i = 0; i < listaParadas.size(); i++) {
			if (Long.parseLong(datos[5]) == listaParadas.get(i).getId()) {

				datosP[1] = listaParadas.get(i).getNombre();
				datosP[2] = listaParadas.get(i).getRegion();

			}
		}
		// Obtener el valor de los atributos del peregrino que se acaba de registrar
		System.out.println("Bienvenido al sistema, sus datos son los siguientes: ID: " + datos[3] + ", nombre: "
				+ datos[0] + "" + ", nacionalidad: " + datos[4] + ", fecha de expedición : " + LocalDateTime.now()
				+ ", nombre + región de la parada inicial: " + datosP[1] + " " + datosP[2]);

		System.out.println("1-Logout");
		res = teclado.nextInt();
		while (res != 2) {
			if (res == 1) {
				// logout
				// logut establezco el usuario que esta conectado a null
				usuarioLogeado = null;
				this.run();
			}

		}

	}

	public void exportar() {

	}

}
