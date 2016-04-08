/**
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

class ASNProjectOK extends ASNObj implements Cloneable {
    private int code;
    
    public ASNProjectOK () {
    }
    
    public ASNProjectOK (int code) {
        this.code = code;
    } 
    
    @Override
    public Encoder getEncoder() {
    
        Encoder task = new Encoder()
        .initSequence()
        .addToSequence(new Encoder(code));
        //task.print();
        
        return task.setASN1Type(Encoder.CLASS_UNIVERSAL);
    }

    @Override
    public ASNProjectOK decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();
        code = decoder.getFirstObject(true).getInteger(Encoder.TAG_INTEGER).intValue();
        if (decoder.getTypeByte() != 0) throw new ASN1DecoderFail("Extra objects!");
        return this;
    }
    
    @Override
    public Object clone() {
        try {
            return new ASNProjectOK(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Code: " + code + "\n";
    }
    
    public static void main (String[] args) throws ASN1DecoderFail {
        int code = 0;
        ASNProjectOK write = new ASNProjectOK(code);
        
        byte[] msg = write.encode();
        
        Decoder decoder = new Decoder(msg);
        
        ASNProjectOK read = new ASNProjectOK();
        
        System.out.println(read.decode(decoder).toString());
        
    }
}
