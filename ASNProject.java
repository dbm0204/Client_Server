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

class ASNProject extends ASNObj  implements Cloneable {
    private String name;
    private ASNTask tasks[];

    public ASNProject () {
    }
    
    public ASNProject (String name) {
        this.name = name;
        tasks = null;
    }

    public ASNProject (String name, ASNTask tasks[]) {
        this(name);
        if (tasks != null) {
            this.tasks = new ASNTask[tasks.length];
            for (int i = 0; i < tasks.length; i++) {
                this.tasks[i] = tasks[i];
            }
        }
    }
    
    public void addTask(ASNTask t) {
        int size = (tasks == null) ? 1 : tasks.length + 1; 
        ASNTask tmp[] = new ASNTask[size];
        for (int i = 0; i < size-1; i++) {
            tmp[i] = tasks[i];
        }
        tmp[tmp.length-1] = t;
        tasks = tmp;
    }
    
    public String getName() {
        return name;
    }
    
    public ASNTask[] getTasks() {
        return tasks;
    }

    @Override
    public Encoder getEncoder() {

        Encoder task = new Encoder()
        .initSequence()
        .addToSequence(new Encoder(name))
        .addToSequence(Encoder.getEncoder(tasks)
                .setASN1Type(Encoder.TAG_SEQUENCE));

        //task.print();

        return task.setASN1Type(Encoder.CLASS_APPLICATION);
    }

    @Override
    public ASNProject decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();
        name = decoder.getFirstObject(true).getString();
        //System.out.println(dec.getTypeByte());
        tasks = decoder.getFirstObject(true)
            .getSequenceOf (
                Encoder.CLASS_APPLICATION,
                new ASNTask[0],
                new ASNTask());
        return this;
    }

    @Override
    public Object clone() {
        try {
            return new ASNProject(name, tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        String output = "Name: " + name + "\n";
        if (tasks == null) return output;
        for (int i = 0; i < tasks.length; i++) {
            output += "task: " + tasks[i].toString() + "\n";
        }

        return output;
    }

    public static void main (String[] args) throws ASN1DecoderFail {
        String name = "Write";
        String user = "joe";
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        String ip = "127.0.0.0";
        int port = 1111;
        boolean done = true;
        ASNTask write0 = new ASNTask(name, start, end, user, ip, port, done);
        ASNTask write1 = new ASNTask(name, start, end, user, ip, port, done);
        ASNTask write2 = new ASNTask(name, start, end, user, ip, port, done);
        ASNTask write3 = new ASNTask(name, start, end, user, ip, port, done);
        ASNTask tasks[] = {write0, write1, write2, write3};

        ASNProject project = new ASNProject("project1", tasks);
        byte[] msg = project.encode();

        Decoder decoder = new Decoder(msg);

        ASNProject read = new ASNProject();
        //read.decode(decoder);
        System.out.println(read.decode(decoder).toString());
    }
}
