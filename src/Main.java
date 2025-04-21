import java.util.*;
public class Main
{
    public static class Voto {
        private int id;
        private int votanteId;
        private int candidatoId;
        private String timestamp;

        public Voto(int id, int votanteId, int candidatoId, String timestamp) {
            this.id = id;
            this.votanteId = votanteId;
            this.candidatoId = candidatoId;
            this.timestamp = timestamp;
        }

        public int getId() {
            return id;
        }

        public int getVotanteId() {
            return votanteId;
        }

        public int getCandidatoId() {
            return candidatoId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setVotanteId(int votanteId) {
            this.votanteId = votanteId;
        }

        public void setCandidatoId(int candidatoId) {
            this.candidatoId = candidatoId;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class Candidato {
        private int id;
        private String nombre;
        private String partido;
        private Queue<Voto> votosRecibidos = new LinkedList<>();

        public Candidato(int id, String nombre, String partido) {
            this.id = id;
            this.nombre = nombre;
            this.partido = partido;
        }

        public void agregarVoto(Voto v) {
            votosRecibidos.add(v);
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public Queue<Voto> getVotosRecibidos() {
            return votosRecibidos;
        }

    }

    public static class Votante {
        private int id;
        private String nombre;
        private boolean yaVoto;

        public Votante(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
            this.yaVoto = false;
        }

        public void marcarComoVotado() {
            this.yaVoto = true;
        }

        public int getId() {
            return id;
        }

        public boolean getYaVoto() {
            return yaVoto;
        }

    }

    public static class UrnaElectoral {
        private LinkedList<Candidato> ListaCandidatos = new LinkedList<>();
        private Stack<Voto> historialVotos = new Stack<Voto>();
        private Queue<Voto> votosReportados = new LinkedList<>();
        private int IdCounter;

        public UrnaElectoral() {
        }

        public boolean VerificarVotante(Votante v1) {
            if (v1.getYaVoto() == true) {
                return true;
            } else {
                return false;
            }
        }

        public boolean RegistrarVoto(Votante v1, int CandidatoId) {
            if (VerificarVotante(v1) == false) {
                Scanner teclado = new Scanner(System.in);
                int votanteid = v1.getId();
                String horaVoto;

                System.out.println("Ingrese la hora del voto (En formato hh:mm:ss)");
                horaVoto = teclado.next();
                int nuevoId = IdCounter + 1;
                IdCounter++;

                Voto nuevoVoto = new Voto(nuevoId, votanteid, CandidatoId, horaVoto);

                Candidato candidatoElegido = null;
                for (int i = 0; i < ListaCandidatos.size(); i++) {
                    Candidato c1 = ListaCandidatos.get(i);
                    if (CandidatoId == c1.getId()) {
                        candidatoElegido = c1;
                        break;
                    }
                }
                if (candidatoElegido == null) {
                    System.out.println("El candidato no existe");
                } else {
                    candidatoElegido.agregarVoto(nuevoVoto);
                    historialVotos.push(nuevoVoto);
                    v1.marcarComoVotado();
                    System.out.println("Voto agregado correctamente , el ID del voto es: " + nuevoId);
                }
                return true;
            } else {
                System.out.println("El usuario ya ha votado");
                return false;
            }
        }

        public boolean reportarVoto(Candidato candidato, int idVoto) {
            Queue<Voto> votosOriginal = candidato.getVotosRecibidos();
            Queue<Voto> temporal = new LinkedList<>();

            while (!votosOriginal.isEmpty()) {
                Voto actual = votosOriginal.poll();

                if (actual.getId() == idVoto) {
                    votosReportados.add(actual);
                    System.out.println("El voto con ID: " + idVoto + " ha sido reportado.");

                    while (!votosOriginal.isEmpty()) {
                        temporal.add(votosOriginal.poll());
                    }

                    while (!temporal.isEmpty()) {
                        votosOriginal.add(temporal.poll());
                    }

                    return true;
                } else {
                    temporal.add(actual);
                }
            }

            while (!temporal.isEmpty()) {
                votosOriginal.add(temporal.poll());
            }

            System.out.println("El voto con ID: " + idVoto + " NO se encontró en la lista de votos del candidato.");
            return false;
        }

        public Map<String, Integer> obtenerResultados() {
            Map<String, Integer> resultados = new HashMap<>();

            for (int i = 0; i < ListaCandidatos.size(); i++) {
                Candidato c = ListaCandidatos.get(i);
                String nombreCandidato = c.getNombre();
                int NumeroDevotos = c.getVotosRecibidos().size();
                resultados.put(nombreCandidato, NumeroDevotos);
            }
            return resultados;
        }
    }

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        UrnaElectoral urna = new UrnaElectoral();


        while (true) {
            System.out.println("\n--- Menú de opciones ---");
            System.out.println("1. Agregar Candidato");
            System.out.println("2. Registrar Voto");
            System.out.println("3. Reportar Voto");
            System.out.println("4. Ver Resultados");
            System.out.println("5. Salir");
            System.out.print("Selecciona una opción: ");

            int opcion = teclado.nextInt();
            teclado.nextLine();

            if (opcion == 1) {
                System.out.print("Ingrese el ID del candidato: ");
                int candidatoId = teclado.nextInt();
                teclado.nextLine();
                System.out.print("Ingrese el nombre del candidato: ");
                String candidatoNombre = teclado.nextLine();
                System.out.print("Ingrese el partido del candidato: ");
                String partidoCandidato = teclado.nextLine();
                Candidato nuevoCandidato = new Candidato(candidatoId, candidatoNombre, partidoCandidato);
                urna.ListaCandidatos.add(nuevoCandidato);
                System.out.println("Candidato agregado exitosamente.");
            } else if (opcion == 2) {
                System.out.print("Ingrese el ID del votante: ");
                int idVotante = teclado.nextInt();
                System.out.print("Ingrese el ID del candidato: ");
                int idCandidato = teclado.nextInt();

                boolean yaVoto = false;
                for (int i = 0; i < urna.historialVotos.size(); i++) {
                    Voto voto = urna.historialVotos.get(i);
                    if (voto.getVotanteId() == idVotante) {
                        yaVoto = true;
                        break;
                    }
                }

                if (yaVoto) {
                    System.out.println("Este votante ya ha votado.");
                } else {
                    Votante nuevoVotante = new Votante(idVotante, "Votante" + idVotante);
                    urna.RegistrarVoto(nuevoVotante, idCandidato);
                }
            } else if (opcion == 3) {
                System.out.print("Ingrese el ID del candidato: ");
                int candidatoReportarId = teclado.nextInt();
                System.out.print("Ingrese el ID del voto a reportar: ");
                int idVotoReportar = teclado.nextInt();
                Candidato candidatoReportar = null;

                for (int i = 0; i < urna.ListaCandidatos.size(); i++) {
                    Candidato c = urna.ListaCandidatos.get(i);
                    if (c.getId() == candidatoReportarId) {
                        candidatoReportar = c;
                        break;
                    }
                }

                if (candidatoReportar != null) {
                    urna.reportarVoto(candidatoReportar, idVotoReportar);
                } else {
                    System.out.println("Candidato no encontrado.");
                }
            } else if (opcion == 4) {
                Map<String, Integer> resultados = urna.obtenerResultados();
                System.out.println("\n--- Resultados ---");
                for (Map.Entry<String, Integer> entry : resultados.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue() + " votos");
                }
            } else if (opcion == 5) {
                System.out.println("Saliendo...");
                break;
            } else {
                System.out.println("Opción no válida, por favor selecciona una opción del menú.");
            }
        }
    }



}