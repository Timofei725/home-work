package org_structure;

import java.io.File;
import java.io.IOException;

public class OrgStructureParserTest {
    public static void main(String[] args) throws IOException {
        OrgStructureParser orgStructureParser = new OrgStructureParserImpl();
        System.out.println(orgStructureParser.parseStructure(new File("org_structure/CSV.txt")));

    }
}
