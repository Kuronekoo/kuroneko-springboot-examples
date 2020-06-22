package cn.kuroneko.demos.proxy;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

/**
 * 自定义日期适配器
 *
 * @author liwei
 * @date 2019/5/28 3:22 PM
 */
public class KuronekoDateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Date.class ? (TypeAdapter<T>) new KuronekoDateTypeAdapter() : null;
        }
    };
    /**
     * 分段锁数量，优先使用质数
     */
    private int segmentCount = 1111;

    private final DateFormat[] enUsFormats;
    private final DateFormat[] localFormats;
    private final DateFormat enUsFormat4write;

    public KuronekoDateTypeAdapter() {
        this(null);
    }

    /**
     * 构造方法
     *
     * @param segmentCnt 分段锁数量。若为null , 或小于2,则使用默认值
     */
    public KuronekoDateTypeAdapter(Integer segmentCnt) {
        super();
        if (segmentCnt != null && segmentCnt > 1) {
            segmentCount = segmentCnt;
        }
        enUsFormats = new DateFormat[segmentCount];
        localFormats = new DateFormat[segmentCount];
        enUsFormat4write =
                DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        for (int i = 0; i < segmentCount; i++) {
            enUsFormats[i] = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
            localFormats[i] = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return deserializeToDate(in.nextString());
    }

    private Date deserializeToDate(String json) {
        int index = json == null ? 0 : Math.abs(json.hashCode() % segmentCount);

        DateFormat dateFormat = localFormats[index];
        synchronized (dateFormat) {
            try {
                return dateFormat.parse(json);
            } catch (ParseException ignored) {
            }
        }
        dateFormat = enUsFormats[index];
        synchronized (dateFormat) {
            try {
                return dateFormat.parse(json);
            } catch (ParseException ignored) {
            }
            try {
                return ISO8601Utils.parse(json, new ParsePosition(0));
            } catch (ParseException e) {
                throw new JsonSyntaxException(json, e);
            }
        }
    }

    @Override
    public synchronized void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String dateFormatAsString = enUsFormat4write.format(value);
        out.value(dateFormatAsString);
    }

    public int getSegmentCount() {
        return segmentCount;
    }

}

