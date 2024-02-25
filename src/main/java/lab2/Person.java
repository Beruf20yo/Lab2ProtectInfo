package lab2;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@AllArgsConstructor
public class Person {
    @Getter
    private String name;
    @Getter
    private Map<FileObj, List<Main.Permission>> access;

    public String getPermissions(FileObj file){
        StringBuilder sb = new StringBuilder();
        List<Main.Permission> perms = access.get(file);
        sb.append(perms.get(0).getNameTag());
        if(perms.size() != 1){
            for (int i = 1; i < perms.size(); i++) {
                sb.append(", ").append(perms.get(i).getNameTag());
            }
        }
        return sb.toString();
    }
    public void setPermissions(FileObj file, List<Main.Permission> perms){
        List<Main.Permission> permissions = access.get(file);
        if(permissions.contains(Main.Permission.NO_ACCESS)){
            permissions = new ArrayList<>();
        }
        if(permissions.contains(Main.Permission.FULL_ACCESS)){
            return;
        }
        permissions.addAll(perms);
        access.put(file, new HashSet<>(permissions).stream().toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(var file: access.keySet().stream().sorted(new FileObj.FileComp()).toList()){
            sb.append(file.getFileName()).append(" - ");

            for(var perm: access.get(file)){
                sb.append(perm.getNameTag()).append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
