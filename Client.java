import java.util.Scanner;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option.Builder;

public class Client {

    public static void main(String[] args) {
        HelpFormatter formatter = new HelpFormatter();

        Option tcp_option = Option.builder("t")
            .required(false)
            .longOpt("tcp")
            .desc("start tcp connection (default)")
            .argName("tcp")
            .build();

        Option udp_option = Option.builder("u")
            .required(false)
            .longOpt("udp")
            .desc("start udp connection")
            .argName("udp")
            .build();

        Option help_option = Option.builder("h")
            .longOpt("help")
            .desc("display help menu")
            .build();

        Option port_option = Option.builder("p")
            .required(true)
            .longOpt("port")
            .desc("port of the server to connect to")
            .argName("port")
            .hasArg()
            .build();
            
        Option ip_option = Option.builder("a")
            .required(true)
            .longOpt("address")
            .desc("ip address of the server to connect to")
            .argName("address")
            .build();            
            
        Options options = new Options();
        options.addOption(tcp_option);
        options.addOption(udp_option);
        options.addOption(help_option);
        options.addOption(port_option);
        options.addOption(ip_option);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            String ip = null;
            int port = 0;
            boolean isTCP = true;

            if (cmd.hasOption("h")) {
                formatter.printHelp("TaskServer", options);
                return;
            }

            if (cmd.hasOption("t")) {
                isTCP = true;
            }

            if (cmd.hasOption("u")) {
                isTCP = false;
            }
            
            if (cmd.hasOption("p")) {
                port = Integer.parseInt(cmd.getOptionValue("p"));
            }
             
            if (cmd.hasOption("a")) {
                ip = cmd.getOptionValue("a");
            }

            Parser cmd_parser = new Parser(System.in);
            cmd_parser.setServer(ip, port, isTCP);
            cmd_parser.parse();

        } catch (ParseException pe) {
            System.err.println("Parsing failed: " + pe.getMessage());
            formatter.printHelp("TaskServer", options);
        } catch (NumberFormatException ne) {
            System.err.println("Invalid input for port");
            formatter.printHelp("TaskServer", options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
