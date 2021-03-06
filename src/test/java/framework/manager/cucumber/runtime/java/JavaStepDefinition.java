package test.java.framework.manager.cucumber.runtime.java;

import gherkin.I18n;
import gherkin.formatter.Argument;
import gherkin.formatter.model.Step;
import test.java.framework.manager.cucumber.runtime.*;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Pattern;

class JavaStepDefinition implements StepDefinition {
    private final Method method;
    private final Pattern pattern;
    private final long timeout;
    private final JdkPatternArgumentMatcher argumentMatcher;
    private final test.java.framework.manager.cucumber.runtime.java.ObjectFactory objectFactory;
    private List<ParameterInfo> parameterInfos;

    public JavaStepDefinition(Method method, Pattern pattern, long timeoutMillis, test.java.framework.manager.cucumber.runtime.java.ObjectFactory objectFactory) {
        this.method = method;
        this.parameterInfos = ParameterInfo.fromMethod(method);
        this.pattern = pattern;
        this.argumentMatcher = new JdkPatternArgumentMatcher(pattern);
        this.timeout = timeoutMillis;
        this.objectFactory = objectFactory;
    }

    public void execute(I18n i18n, Object[] args) throws Throwable {
        Utils.invoke(objectFactory.getInstance(method.getDeclaringClass()), method, timeout, args);
    }

    public List<Argument> matchedArguments(Step step) {
        return argumentMatcher.argumentsFrom(step.getName());
    }

    public String getLocation(boolean detail) {
        MethodFormat format = detail ? MethodFormat.FULL : MethodFormat.SHORT;
        return format.format(method);
    }

    @Override
    public Integer getParameterCount() {
        return parameterInfos.size();
    }

    @Override
    public ParameterInfo getParameterType(int n, Type argumentType) {
        return parameterInfos.get(n);
    }

    public boolean isDefinedAt(StackTraceElement e) {
        return e.getClassName().equals(method.getDeclaringClass().getName()) && e.getMethodName().equals(method.getName());
    }

    @Override
    public String getPattern() {
        return pattern.pattern();
    }
}
