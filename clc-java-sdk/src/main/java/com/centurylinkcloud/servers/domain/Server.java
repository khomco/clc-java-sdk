package com.centurylinkcloud.servers.domain;

/**
 * @author ilya.drabenia
 */
public class Server {
    private String name;
    private InstanceType type;
    private Group group;
    private Template template;
    private Machine machine;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Server name(String name) {
        setName(name);
        return this;
    }

    public InstanceType getType() {
        return type;
    }

    public void setType(InstanceType type) {
        this.type = type;
    }

    public Server type(InstanceType type) {
        setType(type);
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Server group(Group group) {
        setGroup(group);
        return this;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Server template(Template template) {
        setTemplate(template);
        return this;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Server machine(Machine machine) {
        setMachine(machine);
        return this;
    }
}
