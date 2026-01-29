package pallo.command;

import org.junit.jupiter.api.Test;
import pallo.exception.PalloException;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parseCommand_byeCommand_returnsByeCommandType() throws PalloException {
        Command result = Parser.parseCommand("bye");
        assertEquals(CommandType.BYE, result.getType());
        assertNull(result.getArgument());
    }

    @Test
    public void parseCommand_listCommand_returnsListCommandType() throws PalloException {
        Command result = Parser.parseCommand("list");
        assertEquals(CommandType.LIST, result.getType());
        assertNull(result.getArgument());
    }

    @Test
    public void parseCommand_caseInsensitive_worksWithUpperCase() throws PalloException {
        Command result = Parser.parseCommand("BYE");
        assertEquals(CommandType.BYE, result.getType());

        result = Parser.parseCommand("LIST");
        assertEquals(CommandType.LIST, result.getType());
    }

    @Test
    public void parseCommand_emptyInput_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand(""));
        assertThrows(PalloException.class, () -> Parser.parseCommand("   "));
    }

    @Test
    public void parseCommand_unknownCommand_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("unknown"));
        assertThrows(PalloException.class, () -> Parser.parseCommand("hello"));
    }

    @Test
    public void parseCommand_todoWithDescription_returnsTodoCommand() throws PalloException {
        Command result = Parser.parseCommand("todo read book");
        assertEquals(CommandType.TODO, result.getType());
        assertEquals("read book", result.getArgument());
    }

    @Test
    public void parseCommand_todoWithoutDescription_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("todo"));
        assertThrows(PalloException.class, () -> Parser.parseCommand("todo "));
    }

    @Test
    public void parseCommand_markWithNumber_returnsMarkCommand() throws PalloException {
        Command result = Parser.parseCommand("mark 1");
        assertEquals(CommandType.MARK, result.getType());
        assertEquals("1", result.getArgument());
    }

    @Test
    public void parseCommand_markWithoutNumber_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("mark"));
    }

    @Test
    public void parseCommand_unmarkWithNumber_returnsUnmarkCommand() throws PalloException {
        Command result = Parser.parseCommand("unmark 2");
        assertEquals(CommandType.UNMARK, result.getType());
        assertEquals("2", result.getArgument());
    }

    @Test
    public void parseCommand_deleteWithNumber_returnsDeleteCommand() throws PalloException {
        Command result = Parser.parseCommand("delete 3");
        assertEquals(CommandType.DELETE, result.getType());
        assertEquals("3", result.getArgument());
    }

    @Test
    public void parseCommand_deleteWithoutNumber_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("delete"));
    }

    @Test
    public void parseCommand_deadlineWithValidFormat_returnsDeadlineCommand() throws PalloException {
        Command result = Parser.parseCommand("deadline return book /by 2024-12-02");
        assertEquals(CommandType.DEADLINE, result.getType());
        Object arg = result.getArgument();
        assertTrue(arg instanceof String[]);
        String[] parts = (String[]) arg;
        assertEquals(2, parts.length);
        assertEquals("return book", parts[0]);
        assertEquals("2024-12-02", parts[1]);
    }

    @Test
    public void parseCommand_deadlineWithoutBy_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("deadline return book"));
    }

    @Test
    public void parseCommand_deadlineEmptyDescription_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("deadline /by 2024-12-02"));
    }

    @Test
    public void parseCommand_deadlineEmptyDate_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("deadline return book /by "));
    }

    @Test
    public void parseCommand_deadlineWithoutArguments_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("deadline"));
    }

    @Test
    public void parseCommand_eventWithValidFormat_returnsEventCommand() throws PalloException {
        Command result = Parser.parseCommand("event project meeting /from Mon 2pm /to 4pm");
        assertEquals(CommandType.EVENT, result.getType());
        Object arg = result.getArgument();
        assertTrue(arg instanceof String[]);
        String[] parts = (String[]) arg;
        assertEquals(3, parts.length);
        assertEquals("project meeting", parts[0]);
        assertEquals("Mon 2pm", parts[1]);
        assertEquals("4pm", parts[2]);
    }

    @Test
    public void parseCommand_eventWithoutFrom_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("event meeting /to 4pm"));
    }

    @Test
    public void parseCommand_eventWithoutTo_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("event meeting /from 2pm"));
    }

    @Test
    public void parseCommand_eventToBeforeFrom_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("event meeting /to 4pm /from 2pm"));
    }

    @Test
    public void parseCommand_eventEmptyDescription_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("event /from 2pm /to 4pm"));
    }

    @Test
    public void parseCommand_eventWithoutArguments_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseCommand("event"));
    }

    @Test
    public void parseTaskNumber_validNumber_returnsInteger() throws PalloException {
        assertEquals(1, Parser.parseTaskNumber("1"));
        assertEquals(42, Parser.parseTaskNumber("42"));
        assertEquals(100, Parser.parseTaskNumber("100"));
    }

    @Test
    public void parseTaskNumber_withWhitespace_returnsInteger() throws PalloException {
        assertEquals(5, Parser.parseTaskNumber("  5  "));
    }

    @Test
    public void parseTaskNumber_invalidNumber_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseTaskNumber("abc"));
        assertThrows(PalloException.class, () -> Parser.parseTaskNumber("1.5"));
        assertThrows(PalloException.class, () -> Parser.parseTaskNumber("one"));
    }

    @Test
    public void parseTaskNumber_emptyInput_throwsPalloException() {
        assertThrows(PalloException.class, () -> Parser.parseTaskNumber(""));
        assertThrows(PalloException.class, () -> Parser.parseTaskNumber("   "));
        assertThrows(PalloException.class, () -> Parser.parseTaskNumber(null));
    }
}
