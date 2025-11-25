package fcai.sclibrary.fuzzyLogic;

import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FuzzyLogicEvaluator, focusing on rule parsing,
 * validation, and error handling scenarios.
 */
public class FuzzyLogicEvaluatorTest {

    private FuzzyLogicEvaluator evaluator;
    private FuzzyVariable temperatureVar;
    private FuzzyVariable humidityVar;
    private FuzzyVariable comfortVar;

    @BeforeEach
    void setUp() {
        evaluator = FuzzyLogicEvaluator.builder().build();

        // Create fuzzy sets for temperature
        FuzzySet cold = new FuzzySet("triangular", "COLD", Arrays.asList(0.0, 10.0, 20.0), null);
        FuzzySet warm = new FuzzySet("triangular", "WARM", Arrays.asList(15.0, 25.0, 35.0), null);
        FuzzySet hot = new FuzzySet("triangular", "HOT", Arrays.asList(30.0, 40.0, 50.0), null);

        // Create fuzzy sets for humidity
        FuzzySet low = new FuzzySet("triangular", "LOW", Arrays.asList(0.0, 20.0, 40.0), null);
        FuzzySet medium = new FuzzySet("triangular", "MEDIUM", Arrays.asList(30.0, 50.0, 70.0), null);
        FuzzySet high = new FuzzySet("triangular", "HIGH", Arrays.asList(60.0, 80.0, 100.0), null);

        // Create fuzzy sets for comfort output
        FuzzySet uncomfortable = new FuzzySet("triangular", "UNCOMFORTABLE", Arrays.asList(0.0, 25.0, 50.0), null);
        FuzzySet comfortable = new FuzzySet("triangular", "COMFORTABLE", Arrays.asList(40.0, 60.0, 80.0), null);
        FuzzySet veryComfortable = new FuzzySet("triangular", "VERY_COMFORTABLE", Arrays.asList(70.0, 85.0, 100.0), null);

        // Create variables
        temperatureVar = new FuzzyVariable(Arrays.asList(cold, warm, hot), "TEMPERATURE");
        humidityVar = new FuzzyVariable(Arrays.asList(low, medium, high), "HUMIDITY");
        comfortVar = new FuzzyVariable(Arrays.asList(uncomfortable, comfortable, veryComfortable), "COMFORT");

        evaluator.setVariables(Arrays.asList(temperatureVar, humidityVar, comfortVar));
    }

    /**
     * Helper method to access private 'rules' field via reflection.
     */
    private List<Rule> getRules() throws Exception {
        Field rulesField = FuzzyLogicEvaluator.class.getDeclaredField("rules");
        rulesField.setAccessible(true);
        return (List<Rule>) rulesField.get(evaluator);
    }

    // ==================== Valid Rule Parsing Tests ====================

    @Test
    void testParseSimpleRule() throws Exception {
        String rule = "IF TEMPERATURE IS WARM\nTHEN COMFORT IS COMFORTABLE";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertEquals(1, rules.size(), "Should have one rule");

        Rule parsedRule = rules.get(0);
        assertEquals(1, parsedRule.getAntecedents().size(), "Should have one antecedent");
        assertEquals(1, parsedRule.getConsequences().size(), "Should have one consequent");

        Rule.Antecedent antecedent = parsedRule.getAntecedents().get(0);
        assertEquals("TEMPERATURE", antecedent.getVar().getName());
        assertEquals("WARM", antecedent.getOutSet().getName());
        assertEquals(Rule.Operator.NONE, antecedent.getOp());

        Rule.Consequent consequent = parsedRule.getConsequences().get(0);
        assertEquals("COMFORT", consequent.getVar().getName());
        assertEquals("COMFORTABLE", consequent.getOutSet().getName());
    }

    @Test
    void testParseRuleWithAndOperator() throws Exception {
        String rule = "IF TEMPERATURE IS WARM\nAND HUMIDITY IS LOW\nTHEN COMFORT IS COMFORTABLE";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertEquals(1, rules.size());

        Rule parsedRule = rules.get(0);
        assertEquals(2, parsedRule.getAntecedents().size(), "Should have two antecedents");

        Rule.Antecedent firstAntecedent = parsedRule.getAntecedents().get(0);
        assertEquals(Rule.Operator.NONE, firstAntecedent.getOp());
        assertEquals("TEMPERATURE", firstAntecedent.getVar().getName());

        Rule.Antecedent secondAntecedent = parsedRule.getAntecedents().get(1);
        assertEquals(Rule.Operator.AND, secondAntecedent.getOp());
        assertEquals("HUMIDITY", secondAntecedent.getVar().getName());
    }

    @Test
    void testParseRuleWithOrOperator() throws Exception {
        String rule = "IF TEMPERATURE IS HOT\nOR HUMIDITY IS HIGH\nTHEN COMFORT IS UNCOMFORTABLE";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertEquals(1, rules.size());

        Rule parsedRule = rules.get(0);
        assertEquals(2, parsedRule.getAntecedents().size());

        Rule.Antecedent firstAntecedent = parsedRule.getAntecedents().get(0);
        assertEquals(Rule.Operator.NONE, firstAntecedent.getOp());

        Rule.Antecedent secondAntecedent = parsedRule.getAntecedents().get(1);
        assertEquals(Rule.Operator.OR, secondAntecedent.getOp());
    }

    @Test
    void testParseRuleWithMixedOperators() throws Exception {
        String rule = "IF TEMPERATURE IS WARM\nAND HUMIDITY IS LOW\nOR HUMIDITY IS MEDIUM\nTHEN COMFORT IS COMFORTABLE";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertEquals(1, rules.size());

        Rule parsedRule = rules.get(0);
        assertEquals(3, parsedRule.getAntecedents().size(), "Should have three antecedents");

        assertEquals(Rule.Operator.NONE, parsedRule.getAntecedents().get(0).getOp());
        assertEquals(Rule.Operator.AND, parsedRule.getAntecedents().get(1).getOp());
        assertEquals(Rule.Operator.OR, parsedRule.getAntecedents().get(2).getOp());
    }

    @Test
    void testParseCaseInsensitiveKeywords() throws Exception {
        String rule = "if TEMPERATURE is WARM\nthen COMFORT is COMFORTABLE";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertEquals(1, rules.size());
        assertEquals(1, rules.get(0).getAntecedents().size());
    }

    @Test
    void testParseMultipleRulesSequentially() throws Exception {
        String rule1 = "IF TEMPERATURE IS COLD\nTHEN COMFORT IS UNCOMFORTABLE";
        String rule2 = "IF TEMPERATURE IS WARM\nTHEN COMFORT IS COMFORTABLE";

        evaluator.create(rule1);
        evaluator.create(rule2);

        List<Rule> rules = getRules();
        assertEquals(2, rules.size(), "Should have two rules after parsing two rules");
    }

    @Test
    void testRuleStringIsStored() throws Exception {
        String rule = "IF TEMPERATURE IS WARM\nTHEN COMFORT IS COMFORTABLE";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertEquals(rule, rules.get(0).getString(), "Original rule string should be stored");
    }

    // ==================== Comment and Empty Line Handling Tests ====================

    @Test
    void testParseRuleWithCommentLines() throws Exception {
        String rule = "# This is a comment\nIF TEMPERATURE IS WARM\n# Another comment\nTHEN COMFORT IS COMFORTABLE";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertEquals(1, rules.size(), "Should parse rule ignoring comment lines");
    }

    @Test
    void testParseRuleWithEmptyLines() throws Exception {
        String rule = "\nIF TEMPERATURE IS WARM\n\nTHEN COMFORT IS COMFORTABLE\n";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertEquals(1, rules.size(), "Should parse rule ignoring empty lines");
    }

    @Test
    void testParseRuleWithOnlyComments() throws Exception {
        String rule = "# This is just a comment\n# Another comment";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        // Should not throw, and rules list should be empty (no valid rules to parse)
        assertTrue(rules.isEmpty(), "Should not create rules from only comments");
    }

    // ==================== Error Handling Tests ====================

    @Test
    void testThrowsExceptionForInvalidStartKeyword() {
        String rule = "WHEN TEMPERATURE IS WARM\nTHEN COMFORT IS COMFORTABLE";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.create(rule)
        );
        assertEquals("Invalid rule format", exception.getMessage());
    }

    @Test
    void testThrowsExceptionForMissingIsKeyword() {
        String rule = "IF TEMPERATURE EQUALS WARM\nTHEN COMFORT IS COMFORTABLE";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.create(rule)
        );
        assertEquals("Invalid rule format", exception.getMessage());
    }

    @Test
    void testThrowsExceptionForTooFewTokens() {
        String rule = "IF TEMPERATURE\nTHEN COMFORT IS COMFORTABLE";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.create(rule)
        );
        assertEquals("Invalid rule format", exception.getMessage());
    }

    @Test
    void testThrowsExceptionForUnknownVariable() {
        String rule = "IF PRESSURE IS HIGH\nTHEN COMFORT IS COMFORTABLE";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.create(rule)
        );
        assertTrue(exception.getMessage().contains("Variable PRESSURE not found"));
    }

    @Test
    void testThrowsExceptionForUnknownFuzzySet() {
        String rule = "IF TEMPERATURE IS FREEZING\nTHEN COMFORT IS COMFORTABLE";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.create(rule)
        );
        assertTrue(exception.getMessage().contains("Set FREEZING not found"));
    }

    @Test
    void testThrowsExceptionForUnknownConsequentVariable() {
        String rule = "IF TEMPERATURE IS WARM\nTHEN UNKNOWN IS COMFORTABLE";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.create(rule)
        );
        assertTrue(exception.getMessage().contains("Variable UNKNOWN not found"));
    }

    @Test
    void testThrowsExceptionForUnknownConsequentSet() {
        String rule = "IF TEMPERATURE IS WARM\nTHEN COMFORT IS AMAZING";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.create(rule)
        );
        assertTrue(exception.getMessage().contains("Set AMAZING not found"));
    }

    // ==================== Rule Edit Tests ====================

    @Test
    void testEditRule() throws Exception {
        String rule1 = "IF TEMPERATURE IS COLD\nTHEN COMFORT IS UNCOMFORTABLE";
        String rule2 = "IF TEMPERATURE IS WARM\nTHEN COMFORT IS COMFORTABLE";

        evaluator.create(rule1);
        evaluator.edit(0, rule2);

        List<Rule> rules = getRules();
        assertEquals(1, rules.size(), "Should still have one rule after editing");

        Rule editedRule = rules.get(0);
        assertEquals("WARM", editedRule.getAntecedents().get(0).getOutSet().getName());
        assertEquals("COMFORTABLE", editedRule.getConsequences().get(0).getOutSet().getName());
    }

    // ==================== Rule Enable/Disable Tests ====================

    @Test
    void testDisableRule() throws Exception {
        String rule = "IF TEMPERATURE IS WARM\nTHEN COMFORT IS COMFORTABLE";
        evaluator.create(rule);

        evaluator.disable(0);

        List<Rule> rules = getRules();
        assertFalse(rules.get(0).isEnabled(), "Rule should be disabled");
    }

    @Test
    void testEnableRule() throws Exception {
        String rule = "IF TEMPERATURE IS WARM\nTHEN COMFORT IS COMFORTABLE";
        evaluator.create(rule);

        evaluator.disable(0);
        evaluator.enable(0);

        List<Rule> rules = getRules();
        assertTrue(rules.get(0).isEnabled(), "Rule should be re-enabled");
    }

    @Test
    void testNewRulesAreEnabledByDefault() throws Exception {
        String rule = "IF TEMPERATURE IS WARM\nTHEN COMFORT IS COMFORTABLE";
        evaluator.create(rule);

        List<Rule> rules = getRules();
        assertTrue(rules.get(0).isEnabled(), "New rules should be enabled by default");
    }
}
