/**
 * Project ::= [1] SEQUENCE {
 *    proj UTF8String,
 *    task UTF8String,
 *    user UTF8String
 * }
 *
 */

import net.ddp2p.ASN1.*;
import java.util.Calendar;
import java.math.BigInteger;
import java.lang.Cloneable;

class ASNTake extends ASNObj implements Cloneable {
    private String proj;
    private String task;
    private String user;
    
    public ASNTake () {
    }
    
    public ASNTake (String proj, String task, String user) {
        this.proj = proj;
        this.task = task;
        this.user = user;
    } 
    
    public String getProject() {
        return proj;
    }
    
    public String getTask() {
        return task;
    }
    
    public String getUser() {
        return user;
    }
    
    @Override
    public Encoder getEncoder() {
    
        Encoder encoder = new Encoder()
        .initSequence()
        .addToSequence(new Encoder(proj))
        .addToSequence(new Encoder(task))
        .addToSequence(new Encoder(user));
        //task.print();
        
        return encoder.setASN1Type(Encoder.CLASS_UNIVERSAL);
    }

    @Override
    public ASNTake decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();
        proj = decoder.getFirstObject(true).getString();
        task = decoder.getFirstObject(true).getString();
        user = decoder.getFirstObject(true).getString();
        if (decoder.getTypeByte() != 0) throw new ASN1DecoderFail("Extra objects!");
        return this;
    }
    
    @Override
    public Object clone() {
        try {
            return new ASNTake(proj, task, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Project: " + proj + "\n" +
               "Task: " + task + "\n" +
               "User: " + user + "\n";
    }
    
    public static void main (String[] args) throws ASN1DecoderFail {
        String proj = "Project1";
        String task = "task2";
        String user = "larry";
        
        ASNTake write = new ASNTake(proj, task, user);
        
        byte[] msg = write.encode();
        
        Decoder decoder = new Decoder(msg);
        
        ASNTake read = new ASNTake();
        
        System.out.println(read.decode(decoder).toString());
        
    }
}
