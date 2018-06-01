package ds.made.jdbc.easy.utility;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NamedParameterUtils
{

    /**
     * Parses a query with named parameters. The parameter-index mappings are
     * put into the map, and the parsed query is returned. DO NOT CALL FROM CLIENT CODE.
     *
     * @param query query to parse
     * @param paramMap map to hold parameter-index mappings
     * @return the parsed query
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static String parse(String query, Map paramMap)
    {
        // I was originally using regular expressions, but they didn't work well for ignoring
        // parameter-like strings inside quotes.
        int length = query.length();
        StringBuilder parsedQuery = new StringBuilder(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int index = 1;

        for (int i = 0; i < length; i++)
        {
            char c = query.charAt(i);
            if (inSingleQuote)
            {
                if (c == '\'')
                {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote)
            {
                if (c == '"')
                {
                    inDoubleQuote = false;
                }
            } else
            {
                if (c == '\'')
                {
                    inSingleQuote = true;
                } else if (c == '"')
                {
                    inDoubleQuote = true;
                } else if (c == ':' && i + 1 < length
                        && Character.isJavaIdentifierStart(query.charAt(i + 1)))
                {
                    int j = i + 2;
                    while (j < length && Character.isJavaIdentifierPart(query.charAt(j)))
                    {
                        j++;
                    }
                    String name = query.substring(i + 1, j);
                    c = '?'; // replace the parameter with a question mark
                    i += name.length(); // skip past the end if the parameter

                    List indexList = (List) paramMap.get(name);
                    if (indexList == null)
                    {
                        indexList = new LinkedList();
                        paramMap.put(name, indexList);
                    }
                    indexList.add(index);

                    index++;
                }
            }
            parsedQuery.append(c);
        }

        // replace the lists of Integer objects with arrays of ints
        for (Iterator itr = paramMap.entrySet().iterator(); itr.hasNext();)
        {
            Map.Entry entry = (Map.Entry) itr.next();
            List list = (List) entry.getValue();
            int[] indexes = new int[list.size()];
            int i = 0;
            for (Iterator itr2 = list.iterator(); itr2.hasNext();)
            {
                Integer x = (Integer) itr2.next();
                indexes[i++] = x;
            }
            entry.setValue(indexes);
        }

        return parsedQuery.toString();
    }

}
