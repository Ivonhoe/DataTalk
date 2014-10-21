import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comment unit show in JD android APP
 * 
 * @author Fan.Yang.NB
 */

public class AppCommentUnit {

    public AppCommentUnit(String string) {
        matchNumber(string);
        // matchString(string);
        attribute = new Attribute(string);
    }

    long replyCount;
    long usefulVoteCount;
    long totalCount;
    long score;
    long uselessVoteCount;
    String userId;
    String creationTime;
    String commentId;
    String usernickName;
    Attribute attribute;

    private void matchString(String string) {
        Pattern pattern = Pattern.compile("\"(\\w*?)\":([^\\d]*?),");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            System.out.println("group=" + matcher.group());
            switch (matcher.group(1)) {
                case "userId":

                    break;
                case "creationTime":
                    break;
                case "commentId":
                    break;
                case "usernickName":
                    break;
                default:
                    break;
            }
        }
    }

    private void matchNumber(String string) {
        Pattern pattern = Pattern.compile("\"(\\w*?)\":(\\d*?),");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            // System.out.println("group=" + matcher.group());
            switch (matcher.group(1)) {
                case "replyCount":
                    replyCount = Long.parseLong(matcher.group(2));
                    break;
                case "usefulVoteCount":
                    usefulVoteCount = Long.parseLong(matcher.group(2));
                    break;
                case "totalCount":
                    totalCount = Long.parseLong(matcher.group(2));
                    break;
                case "score":
                    score = Long.parseLong(matcher.group(2));
                    break;
                default:
                    break;
            }
        }
    }

    public String getComment() {
        return attribute.getComment();
    }

    public String getAdvantage() {
        return attribute.getAdvantage();
    }

    public String getDisadvantage() {
        return attribute.getDisadvantage();
    }

    public String getColor() {
        return attribute.getColor();
    }

    public String getMode() {
        return attribute.getMode();
    }

    public String getBuyTime() {
        return attribute.getBuyTime();
    }

    public class Attribute {
        String comment;
        String advantage;
        String disadvantage;
        String color;
        String mode;
        String buyTime;

        Attribute(String string) {
            matchAttribute(string);
        }

        private void matchAttribute(String string) {
            // {"v":"屏幕合适，值得推荐购买，好评","k":"心得"}
            // {"v":"白色","k":"颜色"}
            // {"v":"普通版","k":"型号"}
            // {"v":"2013-07-11 10:58:40","k":"购买日期"}
            Pattern pattern = Pattern.compile("\\{\"v\":\"(.*?)\",\"k\":\"(.*?)\"\\}");
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                // System.out.println(matcher.group());
                switch (matcher.group(2)) {
                    case "心得":
                        comment = matcher.group(1);
                        break;
                    case "优点":
                        advantage = matcher.group(1);
                        break;
                    case "缺点":
                        disadvantage = matcher.group(1);
                        break;
                    case "颜色":
                        color = matcher.group(1);
                        break;
                    case "型号":
                        mode = matcher.group(1);
                        break;
                    case "购买日期":
                        buyTime = matcher.group(1);
                        break;
                    default:
                        break;
                }
            }
            // printAttribute();
        }

        public String getComment() {
            return comment;
        }

        public String getAdvantage() {
            return advantage;
        }

        public String getDisadvantage() {
            return disadvantage;
        }

        public String getColor() {
            return color;
        }

        public String getMode() {
            return mode;
        }

        public String getBuyTime() {
            return buyTime;
        }

        public void printAttribute() {
            System.out.println("心得=" + comment);
            System.out.println("优点=" + advantage);
            System.out.println("缺点=" + disadvantage);
            System.out.println("颜色=" + color);
            System.out.println("型号=" + mode);
            System.out.println("购买日期=" + buyTime);
        }
    }
}
