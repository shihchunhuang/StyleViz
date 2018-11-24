package analysis;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CamelDeclarationIdentifier extends DeclarationProcessor {

    public static JSONObject camelJSON = new JSONObject();

    public static int currentClass;

    private int prevClassCount = 0;
    private int prevMethodCount = 0;
    private int prevStaticVariableCount = 0;
    private int prevVariableCount = 0;

    private boolean snakeCase = false;

    private List<String> goodClassNames = new ArrayList<>();
    private List<String> badClassNames = new ArrayList<>();
    private List<String> goodMethodNames = new ArrayList<>();
    private List<String> badMethodNames= new ArrayList<>();
    private List<String> goodStaticVariableNames= new ArrayList<>();
    private List<String> badStaticVariableNames= new ArrayList<>();
    private List<String> goodVariableNames= new ArrayList<>();
    private List<String> badVariableNames= new ArrayList<>();


    public CamelDeclarationIdentifier(){ }

    @Override
    public void initializeJSON(int classIterator) {

        JSONObject emptyJSON = new JSONObject();
        camelJSON.put("Class"+Integer.toString(classIterator), emptyJSON);
    }

    private List<String> getSubstrings(String name) {
        List<String> substringsOfName = new ArrayList<>();
        int indexOfLastCap = 0;
        // populate substringsOfName with k-1 substrings of class name with k total substrings
        for (int i=1; i<name.length(); i++) {
            if (Character.isUpperCase(name.charAt(i))) {
                String subWord = name.substring(indexOfLastCap, i);
                substringsOfName.add(subWord);
                indexOfLastCap = i;
            }
        }
        // add kth substring to substringsOfName
        String lastChar = Character.toString(name.charAt(name.length()-1));
        String lastSubstring = name.substring(indexOfLastCap, name.length()-1).concat(lastChar);
        substringsOfName.add(lastSubstring);

        return substringsOfName;
    }

    private boolean containsVerifiedSubstrings(List<String> substringsOfName) {

        boolean isValidName = true;

        for (String substring: substringsOfName) {
            if (!DeclarationProcessor.isInDictionary(substring)) {
                isValidName = false;
            }
        }
        return isValidName;
    }

    private void updateJSON(String key, List<String> newValue, List<String> originalValue, int prevTypeCount) {
        JSONObject classValue = (JSONObject) camelJSON.get("Class"+Integer.toString(currentClass));

        int actualNewValue = newValue.size() - prevTypeCount;
        String percentage = Integer.toString(actualNewValue/originalValue.size() * 100);
        classValue.put(key, percentage);
    }

    @Override
    public void checkClassName(List<String> classNames) {

        for (String name: classNames) {

            List<String> substringsOfName;
            boolean isValidName;

            if (Character.isUpperCase(name.charAt(0))) {

                substringsOfName = getSubstrings(name);
                // loop through and check each substring
                isValidName = containsVerifiedSubstrings(substringsOfName);

                if (isValidName) {
                    goodClassNames.add(name);
                } else {
                    badClassNames.add(name);
                }
            } else {
                // name is not valid because doesn't start with a capital
                badClassNames.add(name);
            }
        }
        // find right class in JSON and insert name key and percentage
        updateJSON(NAME_KEY, goodClassNames, classNames, prevClassCount);
        prevClassCount = goodClassNames.size();
    }

    @Override
    public void checkMethodName(List<String> methodNames) {

        boolean constructorIncluded = false;

        for (String method: methodNames) {

            List<String> substringsOfName;

            // check if constructor has been accounted for
            if (Character.isUpperCase(method.charAt(0))) {
                if (!constructorIncluded) {
                    for (String className: goodClassNames) {
                        if (className.equals(method)) {
                            // constructor has been verified to equal name of corresponding class
                            goodMethodNames.add(method);
                            constructorIncluded = true;
                        }
                    }
                } else {
                    // method is not a constructor but starts with a capital = bad
                    badMethodNames.add(method);
                }
            } else {
                // method starts with a lowercase; check if one- or multi-worded
                substringsOfName = getSubstrings(method);

                if (containsVerifiedSubstrings(substringsOfName)) {
                    goodMethodNames.add(method);
                } else {
                    badMethodNames.add(method);
                }
            }
        }
        updateJSON(METHOD_KEY, goodMethodNames, methodNames, prevMethodCount);
        prevMethodCount = goodMethodNames.size();
    }

    @Override
    public void checkStaticVariableName(List<String> staticVariableNames) {
        // TODO use goodStaticVariableNames badStaticVariableNames
        // TODO goodStaticVariableNames.size / staticVariableNames.size x 100 (no percentage) return string
        // TODO update JSON
    }

    @Override
    public void checkVariableName(List<String> variableNames) {
        // TODO use goodVariableNames badVariableNames
        // TODO goodVariableNames.size / variableNames.size x 100 (no percentage) return string
        // TODO update JSON

        // TODO: set SNAKE_CASE_KEY to false
    }

}