/*
 * Project ::= [1] SEQUENCE {
 *    name UTF8String,
 *    tasks SEQUENCE OF Task
 * }
 *
 */

import net.ddp2p.ASN1.*;
import java.util.Calendar;
import java.math.BigInteger;
import java.lang.Cloneable;

class ASNProjectsAnswer extends ASNObj  implements Cloneable {
    private ASNProject projects[];

    public ASNProjectsAnswer () {
    }

    public ASNProjectsAnswer (ASNProject projects[]) {
        this.projects = new ASNProject[projects.length];
        for (int i = 0; i < projects.length; i++) {
            this.projects[i] = projects[i];
        }
    }

    @Override
    public Encoder getEncoder() {

        Encoder task = new Encoder()
        .initSequence()
        .addToSequence(Encoder.getEncoder(projects)
                .setASN1Type(Encoder.TAG_SEQUENCE));

        //task.print();

        return task.setASN1Type(Encoder.CLASS_PRIVATE);
    }

    @Override
    public ASNProjectsAnswer decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();
        projects = decoder.getFirstObject(true)
            .getSequenceOf (
                Encoder.CLASS_APPLICATION,
                new ASNProject[0],
                new ASNProject());
        return this;
    }

    @Override
    public Object clone() {
        try {
            if (projects != null) {
                return new ASNProjectsAnswer(projects);
            }
            return new ASNProjectsAnswer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        String output = "Projects: ";
        if (projects == null) return output;
        for (int i = 0; i < projects.length; i++) {
            output += "project " + i + ": " + projects[i].toString() + "\n";
        }

        return output;
    }

    public static void main (String[] args) throws ASN1DecoderFail {
    	String user = "larry";
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        String ip = "127.0.0.0";
        int port = 1111;
        boolean done = true;
        ASNTask write0 = new ASNTask("John Doe", start, end, user, ip, port, done);
        ASNTask write1 = new ASNTask("John Doe", start, end, user, ip, port, done);
        ASNTask write2 = new ASNTask("John Doe", start, end, user, ip, port, done);
        ASNTask write3 = new ASNTask("John Doe", start, end, user, ip, port, done);
        ASNTask tasks[] = {write0, write1, write2, write3};

        ASNProject project0 = new ASNProject("project1", tasks);
        ASNProject project1 = new ASNProject("project1", tasks);
        ASNProject project2 = new ASNProject("project1", tasks);
        ASNProject project3 = new ASNProject("project1", tasks);

        ASNProject projects[] = {project0, project1, project2, project3};

        ASNProjectsAnswer write = new ASNProjectsAnswer(projects);

        byte[] msg = write.encode();

        Decoder decoder = new Decoder(msg);

        ASNProjectsAnswer read = new ASNProjectsAnswer();
        //read.decode(decoder);
        System.out.println(read.decode(decoder).toString());
    }
}
