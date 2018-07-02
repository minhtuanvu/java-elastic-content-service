package de.funkedigital.fuzo.contentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created By {akremin}
 */
@Service
public class UrlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlService.class);

    private static final String REPLACE[][] = {
            {"[\340\341\342\343\345\346\u0101\u0103\u0105\u01FB\u01FD\u03AC\u03B1\u0430\u1EA1\u1EA3\u1EA5\u1EA7\u1EA9\u1EAB\u1EAD\u1EAF\u1EB1\u1EB3\u1EB5\u1EB7]", "a"},
            {"[\347\u0107\u0109\u010B\u010D\u0441]", "c"},
            {"[\u010F\u0111]", "d"},
            {"[\350\351\352\353\u0113\u0115\u0117\u0119\u011B\u0259\u03AD\u03B5\u0435\u1EB9\u1EBB\u1EBD\u1EBF\u1EC1\u1EC3\u1EC5]", "e"},
            {"[\u011D\u011F\u0121\u0123]", "g"},
            {"[\u0125\u0127]", "h"},
            {"[\u0129\u012B\u012D\u012F\u0131\u0133]", "i"},
            {"[\u0135]", "j"},
            {"[\u0137\u0138\u03BA]", "k"},
            {"[\u013A\u013C\u013E\u0140\u0142\u03BB]", "l"},
            {"[\361\u0144\u0146\u0148\u0149\u014B]", "n"},
            {"[\360\362\363\364\365\370\u014D\u014F\u0151\u01A1\u01D2\u01FF\u03BF\u1ECD\u1ECF\u1ED1\u1ED3\u1ED5\u1ED7\u1ED9\u1EDB\u1EDD\u1EDF\u1EE1\u1EE3]", "o"},
            {"[\u0155\u0157\u0159]", "r"},
            {"[\u015B\u015D\u015F\u0161\u0455]", "s"},
            {"[\u0163\u0165\u0167]", "t"},
            {"[\u0169\u016B\u016D\u016F\u0171\u0173\u01B0\u01D4\u01D6\u01D8\u01DA\u01DC\u03B0\u0446\u1EE5\u1EE7\u1EE9\u1EEB\u1EED\u1EF1]", "u"},
            {"[\u0175\u1E81\u1E83\u1E85]", "w"},
            {"[\u0177\u0443\u04AF\u1EF3\u1EF5\u1EF7\u1EF9]", "y"},
            {"[\u017A\u017C\u017E]", "z"},
            {"[\300\301\302\303\305\306\u0100\u0102\u0104\u01FA\u01FC\u0386\u1EA0\u1EA2\u1EA4\u1EA6\u1EA8\u1EAA\u1EAC\u1EAE\u1EB0\u1EB2\u1EB4\u1EB6]", "A"},
            {"[\307\u0106\u0108\u010A\u010C]", "C"},
            {"[\320\u010E]", "D"},
            {"[\310\311\312\313\u0112\u0114\u0116\u0118\u011A\u0388\u0395\u0401\u1EB8\u1EBA\u1EBC\u1EBE\u1EC0\u1EC2\u1EC4\u1EC6]", "E"},
            {"[\u011C\u011E\u0120\u0122]", "G"},
            {"[\u0124\u0126\u0397]", "H"},
            {"[\314\315\316\317\u0390\u0407\u1EC8\u1ECA]", "I"},
            {"[\u0134]", "J"},
            {"[\u0136]", "K"},
            {"[\u0139\u013B\u013D\u013F\u0141]", "L"},
            {"[\u0143\u0145\u0147\u014A\u039D\u0418\u0419]", "N"},
            {"[\322\323\324\325\330\u014C\u014E\u0150\u01A0\u01D1\u01FE\u039F\u041E\u1ECC\u1ECE\u1ED0\u1ED2\u1ED4\u1ED6\u1ED8\u1EDA\u1EDC\u1EDE\u1EE0\u1EE2]", "O"},
            {"[\u0154\u0156\u0158]", "R"}, {"[\u015A\u015C\u015E\u0160]", "S"}, {"[\u0162\u0164\u0166]", "T"},
            {"[\u0168\u016A\u016C\u016E\u0170\u0172\u01AF\u01D3\u01D5\u01D7\u01D9\u01DB\u1EE4\u1EE6\u1EE8\u1EEA\u1EEC\u1EEE\u1EF0]", "U"},
            {"[\u0174\u1E80\u1E82\u1E84]", "W"}, {"[\u0176\u0178\u1EF2\u1EF4\u1EF6\u1EF8]", "Y"}, {"[\u0179\u017B\u017D]", "Z"},
            {"\304", "Ae"}, {"\326", "Oe"}, {"\334", "Ue"}, {"\344", "ae"}, {"\366", "oe"}, {"\374", "ue"}, {"\337", "ss"}, {"'", ""}};

    public String buildDefaultUrl(JsonNode article) throws IOException {

        StringBuilder url = new StringBuilder();
        url.append("https://www.futurezone.de/");
        url.append(article.get("homeSection").get("directoryPath").asText());
        url.append("article");
        url.append(article.get("articleId").asText());
        url.append('/');
        String title = getArticleOverridingHeadline(article);
        url.append(makeUrlString(title, '-'));
        url.append(".html");

        return url.toString();
    }

    private String getArticleOverridingHeadline(JsonNode article) {

        JsonNode title = null;
        if (article.get("fields") != null) {
            title = article.get("fields").get("overriding_headline");
            if (title == null || StringUtils.isBlank(title.asText())) {
                title = article.get("fields").get("title");
            }
        }
        if (title == null ||StringUtils.isBlank(title.asText())) {
            title = article.get("title");
        }
        return title == null ? null : title.asText();

    }

    private static String makeUrlString(String str, char delim) {

        for (int i = 0; i < REPLACE.length; i++) {
            str = str.replaceAll(REPLACE[i][0], REPLACE[i][1]);
        }

        char chars[] = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((c < '0' || c > '9') && (c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
                chars[i] = delim;
            }
        }

        str = new String(chars);
        str = str.replaceAll(String.valueOf(delim) + "+", String.valueOf(delim));
        if (str.length() > 0 && chars[chars.length - 1] == delim) {
            str = str.substring(0, str.length() - 1);
        }
        if (str.length() > 0 && chars[0] == delim) {
            str = str.substring(1);
        }
        return str;
    }
}
