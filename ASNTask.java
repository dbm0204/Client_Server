/**
 * Task ::= [1] SEQUENCE {
 *    name UTF8String,
 *    start GeneralizedTime,
 *    end GeneralizedTime
 *    ip UTF8String,
 *    port INTEGER,
 *    done BOOLEAN
 * }
 *
 */

import net.ddp2p.ASN1.*;
import java.util.Calendar;
import java.math.BigInteger;
import java.lang.Cloneable;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

class ASNTask extends ASNObj implements Cloneable {
    private String name = null;
    private Calendar start = null;
    private Calendar end = null;
    private String ip = null;
    private int port = 0;
    private boolean done = false;
    
    private String dateReg = "yyyy-MM-dd:HH'h'mm'm'ss's'SSS'Z'";
    private DateFormat fmt = new SimpleDateFormat(dateReg);
    
    public ASNTask () {
    }
    
    public ASNTask (String name, 
                    Calendar start, 
                    Calendar end, 
                    String ip, 
                    int port, 
                    boolean done) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.ip = ip;
        this.port = port;
        this.done = done;
    }
    
    public ASNTask (String name, String start, String end) {
        try {
            this.start = Calendar.getInstance();
            this.end = Calendar.getInstance();
            
            this.start.setTime(fmt.parse(start));
            this.end.setTime(fmt.parse(end));
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
    @Override
    public Encoder getEncoder() {
    
        Encoder task = new Encoder()
        .initSequence()
        .addToSequence(new Encoder(name))
        .addToSequence(new Encoder(start))
        .addToSequence(new Encoder(end))
        .addToSequence(new Encoder(ip))
        .addToSequence(new Encoder(port))
        .addToSequence(new Encoder(done));
        //task.print();
        
        return task.setASN1Type(Encoder.CLASS_APPLICATION);
    }

    @Override
    public ASNTask decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();
        name = decoder.getFirstObject(true).getString();
        start = decoder.getFirstObject(true)
            .getGeneralizedTimeCalender(Encoder.TAG_GeneralizedTime);
        end = decoder.getFirstObject(true)
            .getGeneralizedTimeCalender(Encoder.TAG_GeneralizedTime);
        ip = decoder.getFirstObject(true).getString();
        port = decoder.getFirstObject(true).getInteger(Encoder.TAG_INTEGER).intValue();
        done = decoder.getFirstObject(true).getBoolean();
        if (decoder.getTypeByte() != 0) throw new ASN1DecoderFail("Extra objects!");
        return this;
    }
    
    @Override
    public Object clone() {
        try {
            return new ASNTask(name, start, end, ip, port, done);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Name: " + name + "\n" +
               "start: " + start.getTime() + "\n" +
               "end: " + end.getTime() + "\n" + 
               "ip: " + ip  + "\n" +
               "port: " + port + "\n" +
               "done: " + done + "\n";
    }
    
    public static void main (String[] args) throws ASN1DecoderFail {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        String ip = "127.0.0.0";
        int port = 1111;
        boolean done = true;
        ASNTask write = new ASNTask("John Doe", start, end, ip, port, done);
        
        byte[] msg = write.encode();
        
        Decoder decoder = new Decoder(msg);
        
        ASNTask read = new ASNTask();
        
        System.out.println(read.decode(decoder).toString());
        
    }
}
