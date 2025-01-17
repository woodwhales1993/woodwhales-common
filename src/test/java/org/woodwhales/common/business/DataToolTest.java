package org.woodwhales.common.business;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.woodwhales.common.example.model.business.DataToolTempData;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DataToolTest {

    @Test
    public void enumMap2() {
        Map<String, DemoEnum> map = DataTool.enumMap(DemoEnum.class);
        assertEquals(3, map.size());
        printMap(map);
    }

    @Test
    public void enumMap1() {
        Map<Integer, DemoEnum> map = DataTool.enumMap(DemoEnum.class, DemoEnum::getCode);
        assertEquals(3, map.size());
        printMap(map);

        boolean containsKey = DataTool.enumContainsKey(1, DemoEnum.class, DemoEnum::getCode);
        assertEquals(true, containsKey);

        DemoEnum demoEnum = DataTool.enumGetValue(2, DemoEnum.class, DemoEnum::getCode);
        assertEquals(DemoEnum.GREEN, demoEnum);

        DemoEnum demoEnum2 = DataTool.enumGetValue(4, DemoEnum.class, DemoEnum::getCode);
        assertNull(demoEnum2);
    }

    @Test
    public void testToList() {
        HashMap<Integer, MapDemo1> map = new HashMap<>();
        map.put(1, new MapDemo1(1, new MapDemo2(1, "map1")));
        map.put(2, new MapDemo1(2, new MapDemo2(2, "map2")));
        map.put(3, new MapDemo1(3, new MapDemo2(3, "map3")));

        List<ListDemo3> list = new ArrayList<>();
        list.add(new ListDemo3(1, "A1"));
        list.add(new ListDemo3(2, "A2"));
        list.add(new ListDemo3(3, "A3"));
        list.add(new ListDemo3(4, "A4"));

        List<ListResult> listResults = DataTool.toListWithMap(list, map, ListDemo3::getKey, (listDemo3, mapDemo1) -> {
            Integer key = listDemo3.getKey();
            MapDemo2 mapDemo2 = mapDemo1.getMapDemo2();
            return new ListResult(key, mapDemo2);
        });

        listResults.forEach(System.out::println);

        System.out.println("=========");

        List<ListResult> listResults2 = DataTool.toListWithMap(list, map, ListDemo3::getKey, (listDemo3, mapDemo1) -> {
            Integer key = listDemo3.getKey();
            MapDemo2 mapDemo2 = mapDemo1.getMapDemo2();
            return new ListResult(key, mapDemo2);
        }, listDemo3 -> {
            Integer key = listDemo3.getKey();
            return new ListResult(key, null);
        });

        listResults2.forEach(System.out::println);
    }

    @Data
    @AllArgsConstructor
    private static class ListResult {
        private Integer listDemo3Key;
        private MapDemo2 mapDemo2;
    }

    @Data
    @AllArgsConstructor
    private static class MapDemo1 {
        private Integer key;
        private MapDemo2 mapDemo2;
    }

    @Data
    @AllArgsConstructor
    private static class MapDemo2 {
        private Integer key;
        private String value;
    }

    @Data
    @AllArgsConstructor
    private static class ListDemo3 {
        private Integer key;
        private String value;
    }

    enum DemoEnum {
        YELLOW(1, "黄色"),
        GREEN(2, "绿色"),
        BLUE(3, "蓝色"),
        ;

        private final Integer code;
        private final String description;

        DemoEnum(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "DemoEnum{" +
                    "code=" + code +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    @Test
    public void toMap2() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        DemoData demoData1 = new DemoData(2, "李四", "描述-李四");
        list.add(demoData1);
        DemoData demoData2 = new DemoData(2, "李四2", "描述-李四2");
        list.add(demoData2);
        list.add(new DemoData(3, "王五", "描述-王五"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));

        Map<Integer, DemoData> map1 = DataTool.toMapForSaveOld(list, DemoData::getId);

        assertEquals(list.size() - 1, map1.size());
        assertEquals(demoData1, map1.get(2));
        printMap(map1);

        System.out.println();

        Map<Integer, DemoData> map2 = DataTool.toMapForSaveNew(list, DemoData::getId);

        assertEquals(list.size() - 1, map2.size());
        assertEquals(demoData2, map2.get(2));
        printMap(map2);
    }

    @Test
    public void toMap1() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        list.add(new DemoData(2, "李四", "描述-李四"));
        list.add(new DemoData(3, "王五", "描述-王五"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));

        Map<Integer, DemoData> integerDemoDataMap = DataTool.toMap(list, DemoData::getId);

        assertEquals(list.size(), integerDemoDataMap.size());
        printMap(integerDemoDataMap);
    }

    @Test
    public void toList() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        list.add(new DemoData(2, "李四", "描述-李四"));
        list.add(new DemoData(3, "王五", "描述-王五"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));
        List<String> resultList = DataTool.toList(list, DemoData::getName);
        assertEquals(list.get(0).getName(), resultList.get(0));
        assertEquals(list.get(2).getName(), resultList.get(2));
    }

    private void printMap(Map map) {
        Map<Object, Object> map1 = (Map<Object, Object>) map;
        map1.entrySet().forEach(entry -> {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(String.format("key = %s, value = %s", key, value));
        });
    }

    @Test
    public void groupingBy() {
        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "张三", "描述-张三"));
        list.add(new DemoData(2, "李四", "描述-李四"));
        list.add(new DemoData(2, "李四2", "描述-李四2"));
        list.add(new DemoData(3, "王五", "描述-王五"));
        list.add(new DemoData(4, "宋八", "描述-宋八"));

        Map<Integer, List<DemoData>> map = DataTool.groupingBy(list, DemoData::getId);
        assertEquals(list.size() - 1, map.size());
        assertEquals(2, map.get(2).size());
        printMap(map);
    }

    @Test
    public void deduplicate() {
        List<DataToolTempData> list = new ArrayList<>();
        DataToolTempData data1 = new DataToolTempData(1, "张三", "描述-张三");
        DataToolTempData data2 = new DataToolTempData(2, "李四", "描述-李四");
        DataToolTempData data3 = new DataToolTempData(3, "王五", "描述-王五");
        DataToolTempData data4 = new DataToolTempData(null, "赵六", "描述-赵六");
        DataToolTempData data5 = new DataToolTempData(3, "朱七", "描述-朱七");
        DataToolTempData data6 = new DataToolTempData(4, "宋八", "描述-宋八");

        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        list.add(data6);

        DeduplicateResult<Integer, DataToolTempData> deduplicateResult1 = DataTool.deduplicate(list,
                data -> Objects.nonNull(data.getId()),
                DataToolTempData::getId, true);

        List<DataToolTempData> deduplicatedList = deduplicateResult1.getDeduplicatedList();
        List<DataToolTempData> repetitiveList = deduplicateResult1.getRepetitiveList();
        List<DataToolTempData> invalidList = deduplicateResult1.getInvalidList();
        List<Integer> deduplicatedKeyList = deduplicateResult1.getDeduplicatedKeyList();

        System.out.println("deduplicatedList");
        deduplicatedList.stream().forEach(System.out::println);

        System.out.println("repetitiveList");
        repetitiveList.stream().forEach(System.out::println);

        System.out.println("invalidList");
        invalidList.stream().forEach(System.out::println);

        System.out.println("deduplicatedKeyList");
        System.out.println(deduplicatedKeyList);

        // 无效数据
        assertEquals(1, invalidList.size());
        assertEquals(data4, invalidList.get(0));

        // 已去重数据
        assertEquals(4, deduplicatedList.size());

        // 重复数据
        assertEquals(1, repetitiveList.size());
        assertEquals(data5, repetitiveList.get(0));

        DeduplicateResult<Integer, DataToolTempData> deduplicateResult2 = DataTool.deduplicate(list,
                data -> Objects.nonNull(data.getId()),
                DataToolTempData::getId, false);
        List<DataToolTempData> deduplicatedList2 = deduplicateResult2.getDeduplicatedList();
        List<DataToolTempData> repetitiveList2 = deduplicateResult2.getRepetitiveList();
        // 重复数据
        assertEquals(4, deduplicatedList2.size());
        assertEquals(data3, repetitiveList2.get(0));

        DeduplicateResult<Integer, DataToolTempData> deduplicateResult3 = DataTool.deduplicate(list, DataToolTempData::getId);
        List<DataToolTempData> deduplicatedList1 = deduplicateResult3.getDeduplicatedList();
        assertEquals(5, deduplicatedList1.size());
    }

    @Test
    public void getDataFromList() {
        DemoDataDTO demoDataDTO = new DemoDataDTO(3, "上海");

        List<DemoData> list = new ArrayList<>();
        list.add(new DemoData(1, "北京", "北京"));
        list.add(new DemoData(2, "南京", "南京"));
        list.add(new DemoData(3, "上海", "上海"));

        DemoData result = DataTool.getDataFromList(demoDataDTO, DemoDataDTO::getId, list, DemoData::getId);
        System.out.println("result = " + result);
        assertEquals(list.get(2), result);
    }

    @Test
    public void handleMap() {
        Map<Integer, HandleMapDTO> map = new HashMap<>();
        map.put(1, new HandleMapDTO(1, new DemoDataDTO(1, "AA")));
        map.put(2, new HandleMapDTO(2, new DemoDataDTO(2, "BB")));
        map.put(2, new HandleMapDTO(3, new DemoDataDTO(3, "CC")));
        System.out.println("new Gson().toJson(map) = " + new Gson().toJson(map));
        DataTool.handleMap(map, (k, v) -> {
            if("AA".equals(k)) {
                new DemoDataDTO(4, "DD");
            };
        });
        System.out.println("new Gson().toJson(map) = " + new Gson().toJson(map));
    }

    class HandleMapDTO {
        private Integer id;
        private DemoDataDTO demoDataDTO;

        public HandleMapDTO(Integer id, DemoDataDTO demoDataDTO) {
            this.id = id;
            this.demoDataDTO = demoDataDTO;
        }
    }

    class DemoDataDTO {
        private Integer id;
        private String nameForVO;

        public DemoDataDTO(Integer id, String nameForVO) {
            this.id = id;
            this.nameForVO = nameForVO;
        }

        public Integer getId() {
            return id;
        }

        public String getNameForVO() {
            return nameForVO;
        }

        @Override
        public String toString() {
            return "DemoDataDTO{" +
                    "id=" + id +
                    ", nameForVO='" + nameForVO + '\'' +
                    '}';
        }
    }

    class DemoData {

        private Integer id;
        private String name;
        private String description;

        public DemoData(Integer id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "DemoData{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    @Test
    public void testMapToList() {
        Map<String, DemoDataDTO> map = new HashMap<>();
        map.put("A", new DemoDataDTO(1, "AA"));
        map.put("B", new DemoDataDTO(2, "BB"));
        map.put("C", new DemoDataDTO(3, "CC"));
        List<String> keyList = DataTool.mapToList(map, (key, value) -> key);
        List<String> valueList = DataTool.mapToList(map, (key, value) -> value.toString());
        System.out.println("keyList = " + keyList);
        System.out.println("valueList = " + valueList);

        List<String> filteredList = DataTool.mapToList(map, (key, value) -> key.equals("A"), (key, value) -> value.toString());
        System.out.println("filteredList = " + filteredList);

        List<String> valueList2 = DataTool.mapValueToList(map, DemoDataDTO::getNameForVO);
        System.out.println("valueList2 = " + valueList2);
    }

}