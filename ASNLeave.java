/*
 *
 * Leave ::= [2] Register
 * 
 */
 
import net.ddp2p.ASN1.*;
import java.util.Calendar;
import java.math.BigInteger;
import java.lang.Cloneable;

class ASNLeave extends ASNObj implements Cloneable {
    private String group;
        
    public ASNLeave () {
    }
    
    public ASNLeave (String group) {
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
        
        return encoder.setASN1Type(Encoder.CLASS_CONTEXT);
    }

    @Override
    public ASNLeave decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();
        group = decoder.getFirstObject(true).getString();
        if (decoder.getTypeByte() != 0) throw new ASN1DecoderFail("Extra objects!");
        return this;
    }
    
    @Override
    public Object clone() {
        try {
            return new ASNLeave(group);
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
        ASNLeave write = new ASNLeave();
        
        byte[] msg = write.encode();
        
        Decoder decoder = new Decoder(msg);
        
        ASNLeave read = new ASNLeave();
        
        System.out.println(read.decode(decoder).toString());  
    }
}
