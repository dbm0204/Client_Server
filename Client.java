import java.util.Scanner;
import java.util.Calendar;
import net.ddp2p.ASN1.Encoder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

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
        
        Option command_option = Option.builder("c")
            .required(true)
            .longOpt("command")
            .desc("command to run on server")
            .argName("command")
            .hasArg()
            .build();

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


        Options options = new Options();
        options.addOption(command_option);
        options.addOption(tcp_option);
        options.addOption(udp_option);
        options.addOption(help_option);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            String command = null;
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

            if (cmd.hasOption("c")) {
                command = cmd.getOptionValue("c");
            }
            assert(command != null);


            InputStream stream;
            stream = new ByteArrayInputStream(command.getBytes());
            Parser cmd_parser = new Parser(stream);
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
