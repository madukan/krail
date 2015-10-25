/*
 * Copyright (c) 2015. David Sowerby
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package uk.q3c.krail.core.services
import spock.lang.Specification
import uk.q3c.krail.UnitTestFor
import uk.q3c.krail.i18n.LabelKey
import uk.q3c.krail.i18n.Translate
import uk.q3c.util.testutil.LogMonitor
/**
 *
 * Created by David Sowerby on 11/11/15.
 */
@UnitTestFor(DefaultServiceDependencyScanner)
class DefaultServiceDependencyScannerTest extends Specification {

    Service mockA = Mock(Service)
    Service mockB = Mock(Service)
    Service mockC = Mock(Service)
    Service mockD = Mock(Service)

    def controller = Mock(ServicesController)
    def translate = Mock(Translate)
    LogMonitor logMonitor



    def setup() {
        mockA.getServiceKey() >> new ServiceKey(LabelKey.Authentication)
        mockB.getServiceKey() >> new ServiceKey(LabelKey.Application_Configuration_Service)
        mockC.getServiceKey() >> new ServiceKey(LabelKey.Active_Source)
        mockD.getServiceKey() >> new ServiceKey(LabelKey.Auto_Stub)
        translate.from(LabelKey.Yes) >> "Yes"
        logMonitor = new LogMonitor()
        logMonitor.addClassFilter(DefaultServiceDependencyScanner.class)
    }

    def cleanup() {
        logMonitor.close()
    }

    def "scan valid dependencies, @Dependency on non-Service field ignored"() {
        given:

        def graph = Mock(ServicesGraph)
        def scanner = new DefaultServiceDependencyScanner(graph)
        def service = new TestService(translate, controller, mockA, mockB, mockC, mockD)

        when:
        scanner.scan(service)

        then:
        1 * graph.alwaysDependsOn(service.getServiceKey(), mockA.getServiceKey())
        1 * graph.optionallyUses(service.getServiceKey(), mockB.getServiceKey())
        1 * graph.requiresOnlyAtStart(service.getServiceKey(), mockC.getServiceKey())
        1 * graph.optionallyUses(service.getServiceKey(), mockD.getServiceKey())
        logMonitor.warnLogs().isEmpty()

    }


    def "scan with Dependency field null"() {
        given:

        def graph = Mock(ServicesGraph)
        def scanner = new DefaultServiceDependencyScanner(graph)
        def service = new TestService(translate, controller, null, mockB, mockC, mockD)

        when:
        scanner.scan(service)

        then:
//        1 * graph.alwaysDependsOn(service.getServiceKey(), mockA.getServiceKey())
        1 * graph.optionallyUses(service.getServiceKey(), mockB.getServiceKey())
        1 * graph.requiresOnlyAtStart(service.getServiceKey(), mockC.getServiceKey())
        1 * graph.optionallyUses(service.getServiceKey(), mockD.getServiceKey())
        logMonitor.warnLogs().contains("Field is annotated with @Dependency but is null, dependency not set");
    }


}