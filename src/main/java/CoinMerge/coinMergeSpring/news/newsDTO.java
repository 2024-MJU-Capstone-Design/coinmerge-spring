package CoinMerge.coinMergeSpring.news;

public class newsDTO {
    String title;
    String content;
    String link;
    String date;

    public newsDTO(String title, String content, String link, String date) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.date = date;
    }

    @Override
    public String toString() {
        return "newsDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
