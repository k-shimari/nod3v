package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataIdMaps {
	private Map<String, String> ClassIDClassMap = new HashMap<>();;
	private Map<String, String> MethodIDMethodMap = new HashMap<>();

	private Map<String, String> dataidClassMap = new HashMap<>();;
	private Map<String, String> dataidMethodMap = new HashMap<>();
	private Map<String, String> dataidLinenumMap = new HashMap<>();
	private Map<String, List<Recentdata>> dataidRecentdataMap = new HashMap<>();
	private Map<String, FieldInfo> dataidVarMap = new HashMap<>();

	public void createMap(List<String> linesDataids, List<String> linesMethods, List<String> linesRecentdata) {
		createNameMap(linesMethods);
		createIDMap(linesDataids);
		createRecentdataMap(linesRecentdata);
		createVarInfoMap(linesDataids);

	}

	public void createNameMap(List<String> linesMethods) {
		for (String line : linesMethods) {
			String ele[] = line.split(",");
			if (ele.length > 6) {
				ClassIDClassMap.put(ele[0], ele[6]);
				MethodIDMethodMap.put(ele[1], ele[3]);
			} else {
				System.err.println("DataIdMaps.java createNameMap: ele.length < 3");
			}
		}
	}

	public void createIDMap(List<String> linesDataids) {
		for (String line : linesDataids) {
			String ele[] = line.split(",");
			if (ele.length > 3) {
				dataidClassMap.put(ele[0], ClassIDClassMap.get(ele[1]));
				dataidMethodMap.put(ele[0], MethodIDMethodMap.get(ele[2]));
				dataidLinenumMap.put(ele[0], ele[3]);

			} else {
				System.err.println("DataIdMaps.java createIDMap: ele.length < 3");
			}
		}
	}

	/*dataid に recentdata(time,thread,data)のリストを対応付ける*/

	private void createRecentdataMap(List<String> linesRecentdata) {
		for (String line : linesRecentdata) {
			String[] element = splitRecentdata(line);
			String dataid = element[0];
			List<Recentdata> list = new ArrayList<>();
			for (int i = 0; i < element.length / 3 - 1; i++) {
				list.add(new Recentdata(element[3 * i + 3], element[3 * i + 4], element[3 * i + 5]));
			}
			dataidRecentdataMap.put(dataid, list);
		}
	}

	private String[] splitRecentdata(String line) {
		String[] s = line.split(",");
		//TODO dataにStringで,が入った時の例外処理を作る

		return s;
	}

	private void createVarInfoMap(List<String> linesDataids) {
		for (String linedat : linesDataids) {
			String elemdat[] = linedat.split(",");

			if (!linedat.contains("Name"))
				continue;
			/* fieldnameとそれがPUT命令かどうかを取得 */
			FieldInfo fi = new FieldInfo(elemdat);
			if (fi.getisFail())
				continue;
			String dataid = elemdat[0];
			dataidVarMap.put(dataid, fi);
		}
	}

	public Map<String, FieldInfo> getDataidVarMap() {
		return dataidVarMap;
	}

	public Map<String, String> getDataidLinenumMap() {
		return dataidLinenumMap;
	}

	public Map<String, String> getDataidClassMap() {
		return dataidClassMap;
	}

	public Map<String, String> getDataidMethodMap() {
		return dataidMethodMap;
	}

	public Map<String, List<Recentdata>> getDataidRecentdataMap() {
		return dataidRecentdataMap;
	}

}