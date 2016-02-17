package models;

import com.google.gson.annotations.Since;
import models.internal.HzModel;
import play.data.validation.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Link DB table
 *
 * @since 1.0
 */
@Entity(name = "links")
@Table(name = "links",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ident"}))
@Since(1.0)
public class Link extends HzModel {

    @Unique
    @Required
    @MinSize(value = 5, message = "ident.minsize")
    @MaxSize(value = 255, message = "ident.maxsize")
    @Since(1.0)
    private String ident;

    @Required
    @MinSize(value = 5, message = "url.minsize")
    @MaxSize(value = 15613, message = "url.maxsize")
    @Since(1.0)
    private String longUrl;

    public static Link init() {
        return new Link();
    }

    public Link setIdent(String ident) {
        this.ident = ident;
        return this;
    }

    public Link setLongUrl(String longUrl) {
        this.longUrl = longUrl;
        return this;
    }

    public String getIdent() {
        return ident;
    }

    public String getLongUrl() {
        return longUrl;
    }
}
