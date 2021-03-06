/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */
package uk.q3c.krail.core.navigate.sitemap;

import com.google.inject.Inject;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.q3c.util.UtilModule;
import uk.q3c.util.clazz.ClassNameUtils;
import uk.q3c.util.testutil.FileTestUtil;
import uk.q3c.util.testutil.TestResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(MycilaJunitRunner.class)
@GuiceContext({UtilModule.class})
public class LoaderReportBuilderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Inject
    ClassNameUtils classNameUtils;

    @Mock
    MasterSitemap sitemap;

    File templateFile;
    List<SitemapLoader> loaders;
    LoaderReportBuilder lrb;
    private File tempDir;

    @Before
    public void setup() throws URISyntaxException {
        tempDir = temporaryFolder.getRoot();

        templateFile = TestResource.resource(this, "LoadReportBuilderTest.template");
        loaders = new ArrayList<>();
        loaders.add(new MockAnnotationLoader());
        loaders.add(new MockDirectLoader());

        for (SitemapLoader loader : loaders) {
            loader.load(sitemap);
        }

    }

    @Test
    public void buildReport() throws IOException {

        // given

        // when
        lrb = new LoaderReportBuilder(loaders, classNameUtils);
        // then
        System.out.println(lrb.getReport()
                              .toString());
        File reportOutput = new File(tempDir, "temp");
        FileUtils.writeStringToFile(reportOutput, lrb.getReport()
                                                     .toString());


        Optional<String> result = FileTestUtil.compare(reportOutput, templateFile);
        String msg = result.isPresent() ? result.get() : "";
        assertThat(result.isPresent()).overridingErrorMessage(msg)
                                      .isFalse();


        // use this to generate a new template if structure changes
        // FileUtils.writeStringToFile(templateFile, lrb.getReport().toString());
    }

    @SuppressWarnings("Duplicates")
    class MockAnnotationLoader extends SitemapLoaderBase {

        @Override
        public boolean load(MasterSitemap sitemap) {
            addError("a", "Pattern with no params");
            addError("b", "Pattern with {0} params", 1);
            addError("b", "Pattern with {0} params, just as an {1}", 2, "example");
            addWarning("c", "Pattern with {0} params, just as an {1}", 2, "example");
            addInfo("a", "Pattern with {0} params", 1);
            addInfo("a", "Pattern with {0} params", 1);
            addInfo("a", "Pattern with {0} params, just as an {1}", 2, "example");
            return false;
        }
    }


    @SuppressWarnings("Duplicates")
    class MockDirectLoader extends SitemapLoaderBase {

        @Override
        public boolean load(MasterSitemap sitemap) {
            addError("a", "Pattern with no params");
            addError("b", "Pattern with {0} params", 1);
            addError("b", "Pattern with {0} params, just as an {1}", 2, "example");
            addWarning("c", "Pattern with {0} params, just as an {1}", 2, "example");
            addInfo("a", "Pattern with {0} params", 1);
            addInfo("a", "Pattern with {0} params", 1);
            addInfo("a", "Pattern with {0} params, just as an {1}", 2, "example");
            return false;
        }
    }

}
