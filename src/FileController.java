import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.Socket;
class FileController {
    public static void main(String[] args) throws Exception {
//      TODO SEPARATOR FOR COMMANDS.
        Socket socket = null;
        OutputStream outputStream = null;
        PrintWriter out = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        Scanner scanner = new Scanner(System.in);
        String port;
        System.out.println("Welcome to FileController!\n");
        System.out.println("List of available commands:");
        listUsage();
        copyUsage();
        byeUsage();
        helpUsage();
        String command = "";
        do {
            command = scanner.nextLine();
//            command = "copy localhost:9999 ./src/MainLoop.class localhost:1026 lexmark0381.ddns.net:9091";
//            command = "list localhost:9999";
//            command = "copy localhost:9999 file localhost:9999";
            String[] Commands = command.split(" ");
            if (Commands[0].equals("list") || Commands[0].equals("copy") || Commands[0].equals("bye") || Commands[0].equals("help")) {
                if (Commands[0].equals("list")) {
                    if (Commands.length == 2) {
                        String target = Commands[1];
                        String address = target.split(":")[0];
                        try{
                            port = target.split(":")[1];
                        } catch (ArrayIndexOutOfBoundsException ex){
                            System.out.println("No port specified.");
                            listUsage();
                            continue;
                        }
                        System.out.println("Listing files on " + address.toString() + ":" + Integer.parseInt(port));
                        try {
                            socket = new Socket(address, Integer.parseInt(port));
                            outputStream = socket.getOutputStream();
                            out = new PrintWriter(outputStream);
                            inputStream = socket.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(inputStream));
                            String line = null;
                            out.println("list");
                            out.flush();
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                            System.out.println("List done.");
                            socket.close();
                        } catch (Exception ex) {
                            System.out.println("Error during network operations: " + ex);
                        }
                    } else {
                        listUsage();
                    }
                }
                if (Commands[0].equals("copy")) {
                    if (Commands.length >= 4) {
                        String SourceServeraddress = Commands[1].split(":")[0];
                        String SourceServerport = Commands[1].split(":")[1];
                        String file = Commands[2];

                        ArrayList<String> Destinations = new ArrayList<String>();
                        for (int i = 3; i < Commands.length; i++) {
                            Destinations.add(Commands[i].toString());
                        }
                        try {
                            socket = new Socket(SourceServeraddress, Integer.parseInt(SourceServerport));
                            outputStream = socket.getOutputStream();
                            out = new PrintWriter(outputStream);
                            inputStream = socket.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(inputStream));
                            out.println(command);
                            out.flush();
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (Exception ex){
                            System.out.println("Error while connecting to "+ Commands[1]);
                        }
                    } else {
                        copyUsage();
                    }

                }
                if (Commands[0].equals("bye")) {
                    System.out.println("C ya\n");
                    System.exit(0);
                }
                if (Commands[0].equals("help")) {
                    System.out.println("List of available commands:");
                    listUsage();
                    copyUsage();
                    byeUsage();
                    helpUsage();
                }
            } else {
                System.out.println("Command not found.\nType \'help\' to list available commands");
            }
        } while( ! command.equals("bye"));
    }



    private static void listUsage() {
        System.out.println("\tLIST: syntax: list (Address):(Port)\n\tLIST Will list all the files from the server (Address) at port (Port).\n");
    }
    public static void copyUsage() {
        System.out.println("\tCOPY: syntax: copy (Source) (File) ([Destination])\n\tCOPY Will copy (File) file at the moment present in (Source) in all the ([Destination]) servers.\n");
    }
    public static void byeUsage(){
        System.out.println("\tBYE: syntax: bye\n\tBYE will close FileController.\n");
    }

    public static void helpUsage() {
        System.out.println("\tHELP: syntax: help\n\tHELP will display all commands and their usage in FileController.\n");
    }


}
