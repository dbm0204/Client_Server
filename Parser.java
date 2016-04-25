/* Parser.java */
/* Generated By:JavaCC: Do not edit this line. Parser.java */
import net.ddp2p.ASN1.*;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.Socket;

public class Parser implements ParserConstants {
  String ip = null;
  int port = 0;
  boolean isTCP = true;

  Socket socket = null;
  InputStream reader = null;
  OutputStream out = null;

  public static void main(String args[]) {
  }

  public void setServer(String ip, int port, boolean isTCP) {
    this.ip = ip;
    this.port = port;
    this.isTCP = isTCP;

    if (isTCP) {
        try {
            socket = new Socket(ip, port);
            reader = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  }

  final public void parse() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case GET_PROJECT:{
        jj_consume_token(GET_PROJECT);
        jj_consume_token(END);
get_project();
        break;
        }
      case PROJECT_DEFINITION:{
        jj_consume_token(PROJECT_DEFINITION);
        jj_consume_token(COLON);
project_definition();
        break;
        }
      case TAKE:{
        jj_consume_token(TAKE);
        jj_consume_token(END);
take();
        break;
        }
      case GET_PROJECTS:{
        jj_consume_token(GET_PROJECTS);
get_projects();
        break;
        }
      case REGISTER:{
        jj_consume_token(REGISTER);
        jj_consume_token(END);
client_register();
        break;
        }
      case LEAVE:{
        jj_consume_token(LEAVE);
        jj_consume_token(END);
client_leave();
        break;
        }
      default:
        jj_la1[0] = jj_gen;
{if ("" != null) return;}
      }
      jj_consume_token(EOL);
parse();
    } catch (ParseException e) {
Token token=null;
        do {
            token = getNextToken();
        } while (token.kind != EOL);
    }
  }

  final public void project_definition() throws ParseException {Token proj=null, tasks=null; ASNProject project;
    proj = jj_consume_token(VARIABLE);
    jj_consume_token(END);
    jj_consume_token(TASKS);
    jj_consume_token(COLON);
    tasks = jj_consume_token(DIGIT);
    jj_consume_token(END);
project = new ASNProject(proj.toString());
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case VARIABLE:{
        ;
        break;
        }
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      task(project);
    }
ASNEvent event = new ASNEvent(ASNEvent.PROJECT, project);
        send(event.encode());
  }

  final public void task(ASNProject project) throws ParseException {Token task=null, start=null, end=null; boolean success=false;
    task = jj_consume_token(VARIABLE);
    jj_consume_token(END);
    start = jj_consume_token(DATETIME);
    jj_consume_token(END);
    end = jj_consume_token(DATETIME);
    jj_consume_token(END);
project.addTask(new ASNTask(task.toString(),
                                    start.toString(),
                                    end.toString()));
  }

  final public void take() throws ParseException {Token user=null, proj=null, task=null; boolean success=false;
    jj_consume_token(USER);
    jj_consume_token(COLON);
    user = jj_consume_token(VARIABLE);
    jj_consume_token(END);
    jj_consume_token(PROJECT);
    jj_consume_token(COLON);
    proj = jj_consume_token(VARIABLE);
    jj_consume_token(END);
    task = jj_consume_token(VARIABLE);
ASNTake take;
        take = new ASNTake(proj.toString(), task.toString(), user.toString());
        ASNEvent event = new ASNEvent(ASNEvent.TAKE, take);
        send(event.encode());
  }

  final public void get_projects() throws ParseException {
ASNProjects getProjects = new ASNProjects();
        ASNEvent event = new ASNEvent(ASNEvent.PROJECTS, getProjects);
        send(event.encode());
  }

  final public void client_register() throws ParseException {Token group=null;
    group = jj_consume_token(VARIABLE);
ASNRegister clientRegister = new ASNRegister(group.toString());
        ASNEvent event = new ASNEvent(ASNEvent.REGISTER, clientRegister);
        send(event.encode());
  }

  final public void client_leave() throws ParseException {Token group=null;
    group = jj_consume_token(VARIABLE);
ASNLeave leave = new ASNLeave(group.toString());
        ASNEvent event = new ASNEvent(ASNEvent.LEAVE, leave);
        send(event.encode());
  }

  final public void get_project() throws ParseException {Token project=null;
    project = jj_consume_token(VARIABLE);
ASNProject getProject = new ASNProject(project.toString());
        ASNEvent event = new ASNEvent(ASNEvent.GETPROJECT, getProject);
        send(event.encode());
  }

  final public void send(byte[] msg) throws ParseException {
if(isTCP) {
            sendTCP(msg);
        } else {
            sendUDP(msg);
        }
  }

  final public void sendTCP(byte[] msg) throws ParseException {
assert(ip != null);
        try {
            out.write(msg);
            out.flush();

             byte[] buffer = new byte[10000];
             int readBytes = reader.read(buffer);
             Decoder decoder = new Decoder(Arrays.copyOfRange(buffer, 0, readBytes));

             if (!decoder.fetchAll(reader)) {
                    System.out.println("Error: buffer too small");
                    {if ("" != null) return;}
             }

             ASNEvent event = new ASNEvent().decode(decoder);
             System.out.println(event.toString());
             //socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
  }

  final public void sendUDP(byte[] msg) throws ParseException {DatagramSocket socket = null; DatagramPacket packet = null;
assert(ip != null);
        try {
            socket = new DatagramSocket();
            InetAddress IpAddress = InetAddress.getByName(ip);
            packet = new DatagramPacket(msg, msg.length, IpAddress, port);
            socket.send(packet);

            byte[] buffer = new byte[10000];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            Decoder decoder = new Decoder(buffer);
            ASNEvent event = new ASNEvent().decode(decoder);
            System.out.println(event.toString());
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
  }

  /** Generated Token Manager. */
  public ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[2];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x6ca,0x4000000,};
   }

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 2; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[28];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 2; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 28; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
