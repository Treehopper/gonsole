package com.codeaffine.gonsole.internal.gitconsole.interpreter;

import static com.codeaffine.gonsole.test.helper.CompositeRepositoryProviderHelper.createWithSingleChildProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.gonsole.internal.Output;
import com.codeaffine.gonsole.internal.gitconsole.interpreter.GitCommandInterpreter;
import com.codeaffine.gonsole.internal.gitconsole.repository.CompositeRepositoryProvider;
import com.codeaffine.gonsole.pdetest.TemporaryRepositoryRule;
import com.codeaffine.test.util.concurrency.RunInThread;
import com.codeaffine.test.util.concurrency.RunInThreadRule;


public class GitCommandInterpreterPDETest {

  @Rule public final TemporaryRepositoryRule repositories = new TemporaryRepositoryRule();
  @Rule public final RunInThreadRule runInThreadRule = new RunInThreadRule();

  private GitCommandInterpreter interpreter;

  @Test
  @RunInThread
  public void testIsRecognizedFromBackgroundThread() {
    boolean recognized = interpreter.isRecognized( "status" );

    assertThat( recognized ).isTrue();
  }

  @Test
  @RunInThread
  public void testExecuteFromBackgroundThread() {
    String executionResult = interpreter.execute( "status" );

    assertThat( executionResult ).isNull();
  }

  @Before
  public void setUp() {
    new PgmResourceBundle().reset();
    File gitDirectory = repositories.create( "repo" )[ 0 ];
    CompositeRepositoryProvider repositoryProvider = createWithSingleChildProvider( gitDirectory );
    interpreter = new GitCommandInterpreter( mock( Output.class ), repositoryProvider );
  }
}