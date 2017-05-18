/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorclase;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author grijalvaromero
 */
public class Sintaxys {
    public static String[] reservadas={"encender",
    "apagar","habilita"," o "," y ","termometro",
    "puerto","leer","si","declara","+","sensor",
    "*","/","diferente","sino","mientras","hasta",
    "detener","imprime","(",")"};
    private static JTextPane editor;
    private static StyledDocument doc;//guarda un estilo de doc
    private static Style estilo;//confg de un estilo
    private static StyleContext contexto;
    private static AttributeSet attr;
    private static AttributeSet attrNegro,attrOperadores,attrParentesis,attrCiclos;
    
    
    //CONSTRUCTORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
    public Sintaxys(JTextPane _editor){
        this.editor=_editor;
        doc=editor.getStyledDocument();
        estilo=editor.addStyle("miEstilo",null);
        contexto=StyleContext.getDefaultStyleContext();
        attr=contexto.addAttribute(contexto.getEmptySet(),
                                    StyleConstants.Foreground,
                                    Color.RED);
        attrNegro=contexto.addAttribute(contexto.getEmptySet(),
                                    StyleConstants.Foreground,
                                    Color.GRAY);
        attrOperadores=contexto.addAttribute(contexto.getEmptySet(), 
                                    StyleConstants.Foreground,Color.PINK);
         attrParentesis=contexto.addAttribute(contexto.getEmptySet(), 
                                    StyleConstants.Foreground,Color.GREEN);
         attrCiclos=contexto.addAttribute(contexto.getEmptySet(), 
                                    StyleConstants.Foreground,Color.CYAN);
        DefaultStyledDocument doc=new DefaultStyledDocument(){
            public void insertString(int offset, String str,AttributeSet a) throws BadLocationException{
                super.insertString(offset,str,a);//herenciaaaaaaaaaaaaaaa
                String texto=getText(0,getLength());
                int before=findLastNonWordChar(texto,offset);
                if (before < 0) before=0;
                int after= findFirstNonWordChar(texto,offset);
                int wordL=before;
                int wordR =before;
                while(wordR <=after){
                    if(wordR==after || 
                            String.valueOf(texto.charAt(wordR)
                            ).matches("\\W")){
                        if(texto.substring(wordL,wordR)
                                .matches("(\\W)*(encender|apagar|habilita|termometro|detener)")){
                            setCharacterAttributes(wordL,wordR-wordL,attr,false);
                        }else if(texto.substring(wordL, wordR)
                                .matches("(\\W)*(\\+|\\-|\\*|\\/|\\=|imprime|declara)")){
                            setCharacterAttributes(wordL,wordR-wordL,attrOperadores,false);
                        }else if(texto.substring(wordL, wordR)
                                .matches("(\\W)*(\\(|\\))")){
                            setCharacterAttributes(wordL,wordR-wordL,attrParentesis,false);
                        }else if(texto.substring(wordL, wordR)
                                .matches("(\\W)*(o|y|si|mientras|sino|hasta|[\\:\\)]{1})")){
                            setCharacterAttributes(wordL,wordR-wordL,attrCiclos,false);
                        }
                        else{
                            setCharacterAttributes(wordL,wordR-wordL,attrNegro,false);
                        }
                        wordL=wordR;
                    }//llave MATCHES PRIMERO
                    wordR++;
                    
                }//llave whileeeeeeeeeeeee
                
            }//llave insertSTRIIIIIIIIIIIIIIIIIINGGGGGG
            public void remove(int offs,int len)throws BadLocationException{
                //herenciaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                super.remove(offs,len);
                String texto =getText(0,getLength());
                int before =findLastNonWordChar(texto,offs);
                if(before < 0){
                    before=0;
                }
                int after=findFirstNonWordChar(texto,offs);
                if(texto.substring(before,after)
                        .matches("(\\W)*(encender|apagar|habilita|termometro|detener)")){
                    setCharacterAttributes(before,after-before,attr,false);
                    
                }else if(texto.substring(before, after)
                                .matches("(\\W)*(\\+|\\-|\\*\\/|\\=|imprime)")){
                     setCharacterAttributes(before,after-before,attrOperadores,false);
                }
                else if(texto.substring(before, after)
                        //\\(|\\)|\")
                                .matches("\"(\\\\W)*()")){
                     setCharacterAttributes(before,after-before,attrParentesis,false);
                }else if(texto.substring(before, after)
                                .matches("(\\W)*(o|y|si|mientras|sino|hasta|([\\:\\)]{1}))")){
                     setCharacterAttributes(before,after-before,attrCiclos,false);
                }
                
                else{
                    setCharacterAttributes(before,after-before,attrNegro,false);
                }
            }
        };//LLAVE DEFAULT STYLED
       
        Principal.txtCodigo.setStyledDocument(doc);
       
    }//llave CONSTRUCTOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOR
    private int findLastNonWordChar(String text, int index){
        while(--index>=0){
            if(String.valueOf(text.charAt(index)).matches("\\W")){
                break;
            }
        }
        return index;
    }
    private  int findFirstNonWordChar(String text, int index){
        while(index < text.length( )){
            if(String.valueOf(text.charAt(index)).matches("\\W")){
                break;
            }
            index++;
        }
        return index;
    }
    
    public static void pintarPalabra(int indice,String palabra){
      
    }
    public int ContarReservadas(String _texto){
        int cont=0;
        for(int x=0;x<reservadas.length;x++){
            if(_texto.contains(reservadas[x])){
                ++cont;
            }
        }
        System.out.println(cont+"");
        return 0;
    }
    public void verficarSintaxys(){
        String[] renglones=editor.getText().split("\\n");
        String[] expresiones={
            "^imprime\\(\"[a-zA-Z0-9]{1,}[\\)][(.*?)]$",
            "imprime.",
            "habilitar "
        };
        for(int x=0;x<renglones.length;x++){
            String renglon=renglones[x];
            //JOptionPane.showMessageDialog(null,"."+renglon+".");
            for(int letra=0;letra<expresiones.length;letra++){
                Pattern patron=Pattern.compile(expresiones[letra]);
                Matcher m=patron.matcher(renglon);
                if(m.matches()==true){
                    JOptionPane.showMessageDialog(null,"OK");
                   // System.out.println("yeaaaaaaaaa");
                }else{
                    JOptionPane.showMessageDialog(null,"nanay");
                }
            }
            //JOptionPane.showMessageDialog(null,renglones[x]);
        }
    }
    
}
