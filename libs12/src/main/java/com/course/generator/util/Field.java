package com.course.generator.util;

public class Field {

    // course_id
    private String name;

    // courseId
    private String nameHump;

    // CourseId
    private String nameBigHump;

    // Chinese name
    private String nameCn;

    // field type in database: char(8)
    private String type;

    // java type: String
    private String javaType;

    // comment in for one field in a table
    private String comment;

    // can or cannot be null for one field in a table
    private Boolean nullAble;

    // string or char sequence length
    private Integer length;

    // is enum or not
    private Boolean enums;

    // enum constant: COURSE_LEVEL
    private String enumConst;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameHump() {
        return nameHump;
    }

    public void setNameHump(String nameHump) {
        this.nameHump = nameHump;
    }

    public String getNameBigHump() {
        return nameBigHump;
    }

    public void setNameBigHump(String nameBigHump) {
        this.nameBigHump = nameBigHump;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getNullAble() {
        return nullAble;
    }

    public void setNullAble(Boolean nullAble) {
        this.nullAble = nullAble;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getEnums() {
        return enums;
    }

    public void setEnums(Boolean enums) {
        this.enums = enums;
    }

    public String getEnumConst() {
        return enumConst;
    }

    public void setEnumConst(String enumConst) {
        this.enumConst = enumConst;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", nameHump='" + nameHump + '\'' +
                ", nameBigHump='" + nameBigHump + '\'' +
                ", nameCn='" + nameCn + '\'' +
                ", type='" + type + '\'' +
                ", javaType='" + javaType + '\'' +
                ", comment='" + comment + '\'' +
                ", nullAble=" + nullAble +
                ", length=" + length +
                ", enums=" + enums +
                ", enumConst='" + enumConst + '\'' +
                '}';
    }
}

