package cn.nanven.mindmap.util;

import cn.nanven.mindmap.entity.Command;
import cn.nanven.mindmap.common.handler.CommandEventHandler;

public class CommandUtil {
    public static Command generate(CommandEventHandler handler, Object param1, Object param2) {
        return new Command() {
            @Override
            public void execute() {
                handler.execute(param1);
            }

            @Override
            public void undo() {
                handler.execute(param2);
            }
        };
    }
}
