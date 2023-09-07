package org.example;


public class App
{
    public static void main( String[] args ) {

        CrudOsbb osbb = new CrudOsbb();
        osbb.init();
        osbb.selectMembersWithNotAutoAllowed();
    }
}
