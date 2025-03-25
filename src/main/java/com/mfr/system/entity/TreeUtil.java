package com.mfr.system.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TreeUtil {

	public static List<Map<String, Object>> toList(List<? extends Tree> list, int indent){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		int i = 0;
		for(int j = 0; j < list.size(); j++){
			Map<String, Object> obj = new HashMap<String, Object>();
			for(Method m : list.get(j).getClass().getMethods()) {
				if(!m.isAnnotationPresent(JsonIgnore.class)) {
					if((m.getName().startsWith("is") || m.getName().startsWith("get")) && !m.getName().equals("getChild") && !m.getName().equals("getParent")) {
						try {
							String fieldName = m.getName().substring(2);
							if(m.getName().startsWith("get")) {
								fieldName = fieldName.substring(1);
							}
							fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
							obj.put(fieldName , m.invoke(list.get(j)));
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							System.out.println(e.getMessage());
						}
					}
				}
			}
			obj.put("indent", indent);
			if(obj.get("nama") != null) {
				if(!obj.get("nama").equals("Neraca") && !obj.get("nama").equals("Neraca 1") && !obj.get("nama").equals("Neraca 2") && !obj.get("nama").equals("Laba Rugi") && !obj.get("nama").equals("Laba Rugi 1")) {
					result.add(obj);
				}
			}else {
				result.add(obj);
			}
			if(list.get(j).getChild().size() > 0){
				List<Map<String, Object>> child = toList(list.get(j).getChild(), indent + 1);
				for(int k = 0; k < child.size(); k++){
					result.add(child.get(k));
				}
			}
		}
		return result;
	}
	
}
