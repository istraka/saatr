
package org.jboss.qa.tool.saatr.domain.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class ConfigProperty implements Serializable, Comparable<ConfigProperty> {

    public static enum Component {
        TEXT_FIELD, TEXT_AREA
    }

    private String name;

    private String value;

    private Component component = Component.TEXT_FIELD;

    private List<String> options = new ArrayList<>();

    public ConfigProperty(String name, String value, List<String> options) {
        this.name = name;
        this.value = value;
        this.options = options;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConfigProperty other = (ConfigProperty) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public int compareTo(ConfigProperty o) {
        if (this.name == null || o.name == null) {
            return 0;
        }
        return this.name.compareTo(o.name);
    }

}
