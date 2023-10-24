package tarea1.vista;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tarea1.datos.CrearCredenciales;
import tarea1.datos.CrearDAT;
import tarea1.datos.CrearXML;
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
		System.exit(0);
	}

	public void registrar() {

		boolean aux = false;
		String usuario = " ";
		String contraseña = " ";
		String nacionalidad = " ";
		int idParadaActual = 0;
		String respuesta = " ";
		ArrayList<Parada> listaParadas = new ArrayList<Parada>();
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
				teclado.nextLine();
				// Comprabar que no sea null(validacion) "en este caso no hace falta validar"
				// Comprobar que idparadaactual este dentro de las id´s de paradas.dat
				// (verificacion)

				ComprobacionArgumentos.comprueba(!listaParadas.contains(new Parada(idParadaActual)),
						"Error: la id que has pasado, no existe");

				System.out.print("Los datos introducidos son correctos? (S/N) ");

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
		teclado.nextLine();
		while (res != 3) {
			if (res == 1) {
				this.exportar(Long.parseLong(datos[3]), datos[0], datos[4], datosP[1], datosP[2]);
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
		teclado.nextLine();
		String nombreP;
		String codigoP;
		long idMayor = Long.MIN_VALUE;
		boolean aux = false;
		while (res != 3) {
			if (res == 1) {
				// registrar parada
				// su identificador propio, su nombre y código de región, y el nombre del
				// usuario que es administrador/responsable de la misma
				do {
					try {

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

						try {
							listaParadas = LeerDAT.obtenerParadas();
							ComprobacionArgumentos.comprueba(listaParadas.contains(new Parada(nombreP)),
									"Error: Parada en uso, repite el proceso utilizando otro nombre distinto");

						} catch (Exception e) {
							System.out.println(e.getMessage());
							return;
						}
						for (Parada a : listaParadas) {
							if (a.getId() > idMayor)
								idMayor = a.getId();

						}

						try {
							CrearDAT.añadirParada(idMayor + 1, nombreP, codigoP, usuarioLogeado.getNombre());
						} catch (IOException e) {
							e.printStackTrace();
						}
						aux = false;
					} catch (Exception e) {
						System.out.println(e.getMessage());
						aux = true;
					}
				} while (aux == true);

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
		teclado.nextLine();
		while (res != 2) {
			if (res == 1) {
				// logout
				// logut establezco el usuario que esta conectado a null
				usuarioLogeado = null;
				this.run();
			}

		}

	}

	public void exportar(long id, String nombre, String nacionalidad, String nombreP, String regionP) {
		ArrayList<Parada> listaParadas = new ArrayList<Parada>();
		boolean aux = false;
		LocalDateTime fechExp;
		double kmRecorridos;
		int numVips;
		int orden;
		LocalDate fechEstancia;
		try {
			listaParadas = LeerDAT.obtenerParadas();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		do {
			try {
				// id peregrino
				// nombre
				// nacionalidad
				// fech expedicion
				fechExp = LocalDateTime.now();
				// distancia total en km. recorrida (formato xx.x)
				System.out.print("Dime numero de km recorridos con el siguiente formato (formato xx.x): ");
				kmRecorridos = teclado.nextInt();
				teclado.nextLine();
				ComprobacionArgumentos.esInvalido(kmRecorridos + "");
				ComprobacionArgumentos.esNulo(kmRecorridos);
				ComprobacionArgumentos.esVacio(kmRecorridos + "");
				ComprobacionArgumentos.comprueba(kmRecorridos < 0, "Distancia incorrecta");

				// nº de estancias VIP realizadas en total
				System.out.print("Dime numero de estancias VIP´s que has realizado en total: ");
				numVips = teclado.nextInt();
				teclado.nextLine();
				ComprobacionArgumentos.esInvalido(numVips + "");
				ComprobacionArgumentos.esNulo(numVips);
				ComprobacionArgumentos.esVacio(numVips + "");
				ComprobacionArgumentos.comprueba(numVips < 0, "Error: nº de estancias vips negativo");

				// y la colección de paradas (nombre y región) de su ruta hasta el día actual
				// indicando en qué fecha se realizó estancia en ellas
				fechEstancia = LocalDate.now();
				// VIP o NO
				System.out.print("La estacia actual es VIP? (S/N): ");
				String vip = teclado.nextLine();
				ComprobacionArgumentos.esInvalido(vip + "");
				ComprobacionArgumentos.esNulo(vip);
				ComprobacionArgumentos.esVacio(vip + "");
				ComprobacionArgumentos.comprueba(vip.length() > 1,
						"Error: respuesta damasiado larga, usa S para si y N para no");
				ComprobacionArgumentos.comprueba(
						!vip.equals("S") && !vip.equals("s") && !vip.equals("N") && !vip.equals("n"),
						"Vuelve a introducir todos los datos de nuevo");

				System.out.print("Dime la posición de la parada en tu ruta desde la parada inicial: ");
				orden = teclado.nextInt();
				teclado.nextLine();
				ComprobacionArgumentos.esInvalido(orden + "");
				ComprobacionArgumentos.esNulo(orden);
				ComprobacionArgumentos.esVacio(orden + "");
				ComprobacionArgumentos.comprueba(orden < 0, "Error: nº de orden negativo");

				try {
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
					Document doc = docBuilder.newDocument();

					// Elemento raiz: <carnet>
					Element carnet = doc.createElement("carnet");
					doc.appendChild(carnet);

					// Elementos dentro de carnet

					CrearXML.agregarElemento(doc, carnet, "id", id + "");
					CrearXML.agregarElemento(doc, carnet, "fechaexp", fechExp + "");
					CrearXML.agregarElemento(doc, carnet, "expedidoen", "paradaxxx");

					// Elementos dentro de peregrino
					Element peregrino = doc.createElement("peregrino");
					carnet.appendChild(peregrino);
					CrearXML.agregarElemento(doc, peregrino, "nombre", nombre);
					CrearXML.agregarElemento(doc, peregrino, "nacionalidad", nacionalidad);

					CrearXML.agregarElemento(doc, carnet, "hoy", fechEstancia + "");
					CrearXML.agregarElemento(doc, carnet, "distanciatotal", kmRecorridos + "");

					// Elemento paradas
					Element paradas = doc.createElement("paradas");
					carnet.appendChild(paradas);

					// Agregar elementos dentro de "paradas"
					for (int i = 1; i <= listaParadas.size(); i++) {
						Element parada = doc.createElement("parada");

						CrearXML.agregarElemento(doc, parada, "orden", orden + "");
						CrearXML.agregarElemento(doc, parada, "nombre", nombreP);
						CrearXML.agregarElemento(doc, parada, "region", regionP);

						paradas.appendChild(parada);
					}

					// Generar el archivo XML

					CrearXML.escribirArchivo(doc, "archivos/" + nombre + ".xml");

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

				aux = false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				aux = true;
			}
		} while (aux == true);
		this.menuPeregrino();
	}

}