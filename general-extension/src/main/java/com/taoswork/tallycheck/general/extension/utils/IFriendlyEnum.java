package com.taoswork.tallycheck.general.extension.utils;

/**
 * Created by Gao Yuan on 2015/8/21.
 */
public interface IFriendlyEnum<TT> {

    TT getType();
    String getFriendlyType();


    //Enum Template:
    /*
    public enum FriendlyGender implements IFriendlyEnum<Character> {
        Male('m', "male"),
        Female('f', "female");

        private final Character type;
        private final String friendlyType;

        private static final Map<Character, FriendlyGender> typeToEnum = new HashMap<Character, FriendlyGender>();
        static {
            for(FriendlyGender _enum : values()){
                typeToEnum.put(_enum.type, _enum);
            }
        }

        FriendlyGender(char type, String friendlyType) {
            this.type = type;
            this.friendlyType = friendlyType;
        }

        public static FriendlyGender fromType(Character character){
            return typeToEnum.get(character);
        }

        @Override
        public Character getType() {
            return type;
        }

        @Override
        public String getFriendlyType() {
            return friendlyType;
        }
    }
    */
   /*
    @PresentationEnum(unknownEnum = "unknown")
    public enum Gender implements IFriendlyEnum<String> {
        male("M", "male"),
        female("F", "female"),
        unknown("U", "unknown");

        public static final String DEFAULT_CHAR = "U";

        private final String type;
        private final String friendlyType;

        private static final Map<String, Gender> typeToEnum = new HashMap<String, Gender>();
        static {
            for(Gender _enum : values()){
                typeToEnum.put(_enum.type, _enum);
            }
        }

        Gender(String type, String friendlyType) {
            this.type = type;
            this.friendlyType = friendlyType;
        }

        public static Gender fromType(String character){
            return typeToEnum.get(character);
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getFriendlyType() {
            return friendlyType;
        }
    }
    */

}
