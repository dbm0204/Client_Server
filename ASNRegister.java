/*
 *
 * Register ::= [3] SEQUENCE {group UTF8String}
 * 
 */
 
import net.ddp2p.ASN1.*;
import java.util.Calendar;
import java.math.BigInteger;
import java.lang.Cloneable;

class ASNRegister extends ASNObj implements Cloneable {
    private String group;
    
    public ASNRegister () {
    }
    
    public ASNRegister (String group) {
        this.group = group;
    } 
    
    public String getGroup() {
        return group;
    }
    
    @Override
    public Encoder getEncoder() {
    
        Encoder encoder = new Encoder()
        .initSequence()
        .addToSequence(new Encoder(group));
        //task.print();
        
        return encoder.setASN1Type(Encoder.CLASS_PRIVATE);
    }

    @Override
    public ASNRegister decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();
        group = decoder.getFirstObject(true).getString();
        if (decoder.getTypeByte() != 0) throw new ASN1DecoderFail("Extra objects!");
        return this;
    }
    
    @Override
    public Object clone() {
        try {
            return new ASNRegister(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Group: " + group + "\n";
    }
    
    public static void main (String[] args) throws ASN1DecoderFail {
        String group = "group 1";
        
        ASNRegister write = new ASNRegister(group);
        
        byte[] msg = write.encode();
        
        Decoder decoder = new Decoder(msg);
        
        ASNRegister read = new ASNRegister();
        
        System.out.println(read.decode(decoder).toString());
        
    }
}
