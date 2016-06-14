package com.taoswork.tallybook.general.extension.collections;

/**
 * Created by Gao Yuan on 2015/5/28.
 */
public class StringChain<T> extends NodeList<T>{
    public String Separator;
    public String Prefix;
    public String Postfix;

    public final static String STRING_NEW_LINE = "\n";
    public final static String STRING_COMMA = ",";
    public final static String STRING_SPACE = " ";

    public StringChain(){
        this(" ");
    }
    public StringChain(String sep){
        super(true, false);
        Separator = sep;
    }

    public StringChain(String prefix, String sep, String postfix){
        super(true, false);
        setFixes(prefix, sep, postfix);
    }

    public void setAsJsonArray(){
        this.setFixes("[", "]");
        this.Separator = STRING_COMMA;
    }

    public void setFixes(String prefix, String postfix){
        Prefix = prefix;
        Postfix = postfix;
    }

    public void setFixes(String prefix, String sep, String postfix){
        Prefix = prefix;
        Separator = sep;
        Postfix = postfix;
    }

    public void setSeparator(String sep){
        this.Separator = sep;
    }

    @Override
    public String toString() {
        if(this.count() == 0)
            return "";
        boolean hasV = false;
        if(Prefix == null)
            Prefix = "";
        if(Postfix == null)
            Postfix = "";
        StringBuilder sb = new StringBuilder(Prefix);
        for(T node : this.iterator()){
            if(hasV)
                sb.append(Separator);
            hasV = true;
            sb.append(node);
        }
        sb.append(Postfix);
        return sb.toString();
    }

    public static String connectObject(Object... objs){
        StringChain<Object> sc = new StringChain<Object>();
        for(Object obj : objs){
            sc.add(obj);
        }
        return sc.toString();
    }
}
